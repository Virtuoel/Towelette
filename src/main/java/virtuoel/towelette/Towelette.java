package virtuoel.towelette;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.RemovableIdList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.tag.Tag;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.RefreshableStateFactory;
import virtuoel.towelette.api.ToweletteApi;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FoamFixCompatibility;

public class Towelette implements ModInitializer, ToweletteApi
{
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	public static final BiPredicate<Fluid, Identifier> ENTRYPOINT_WHITELIST_PREDICATE = (fluid, id) -> ToweletteApi.ENTRYPOINTS.stream().noneMatch(api -> api.isFluidBlacklisted(fluid, id));
	
	public static final Collection<Identifier> FLUID_ID_BLACKLIST = new HashSet<>();
	
	@Override
	public void onInitialize()
	{
		Optional.ofNullable(ToweletteConfig.DATA.get("fluidBlacklist"))
		.filter(JsonElement::isJsonArray)
		.map(JsonElement::getAsJsonArray)
		.ifPresent(array ->
		{
			array.forEach(element ->
			{
				if(element.isJsonPrimitive())
				{
					FLUID_ID_BLACKLIST.add(new Identifier(element.getAsString()));
				}
			});
		});
		
		refreshBlockStates(
			Registry.FLUID.getIds().stream()
			.filter(f -> ENTRYPOINT_WHITELIST_PREDICATE.test(Registry.FLUID.get(f), f))
			.collect(ImmutableSet.toImmutableSet())
		);
		
		RegistryEntryAddedCallback.event(Registry.FLUID).register(
			(rawId, identifier, object) ->
			{
				if(ENTRYPOINT_WHITELIST_PREDICATE.test(object, identifier))
				{
					refreshBlockStates(ImmutableSet.of(identifier));
				}
			}
		);
		
		RegistryIdRemapCallback.event(Registry.BLOCK).register(remapState ->
		{
			reorderBlockStates();
		});
	}
	
	@Override
	public boolean isFluidBlacklisted(Fluid fluid, Identifier id)
	{
		return !fluid.getDefaultState().isStill() || FLUID_ID_BLACKLIST.contains(id);
	}
	
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	
	public static void refreshBlockStates(final Collection<Identifier> newIds)
	{
		refreshBlockStates(FluidProperty.FLUID, newIds);
	}
	
	public static <V extends Comparable<V>> void refreshBlockStates(final Property<V> property, final Collection<V> newValues)
	{
		refreshStates(
			Registry.BLOCK, Block.STATE_IDS,
			property, newValues,
			Block::getDefaultState, Block::getStateFactory, BlockState::initShapeCache
		);
	}
	
	public static <O, V extends Comparable<V>, S extends PropertyContainer<S>> void refreshStates(final Iterable<O> registry, final IdList<S> stateIdList, final Property<V> property, final Collection<V> newValues, final Function<O, S> defaultStateGetter, final Function<O, StateFactory<O, S>> factoryGetter, final Consumer<S> newStateConsumer)
	{
		final long startTime = System.nanoTime();
		
		final boolean enableDebugLogging = Optional.ofNullable(ToweletteConfig.DATA.get("debug"))
			.filter(JsonElement::isJsonObject)
			.map(JsonElement::getAsJsonObject)
			.map(o -> o.get("logStateRefresh"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean)
			.map(JsonElement::getAsBoolean)
			.orElse(false);
		
		final List<RefreshableStateFactory<O, S>> factoriesToRefresh = new LinkedList<>();
		
		for(final O entry : registry)
		{
			if(defaultStateGetter.apply(entry).getEntries().containsKey(property))
			{
				@SuppressWarnings("unchecked")
				final RefreshableStateFactory<O, S> factory = (RefreshableStateFactory<O, S>) factoryGetter.apply(entry);
				
				factoriesToRefresh.add(factory);
			}
		}
		
		final int entryQuantity = factoriesToRefresh.size();
		
		final Collection<S> newStates = new ConcurrentLinkedQueue<>();
		
		final Collection<CompletableFuture<?>> allFutures = new LinkedList<>();
		
		if(enableDebugLogging)
		{
			LOGGER.info("Refreshing states of {} entries for values(s) {} after {} ns of setup.", entryQuantity, newValues, System.nanoTime() - startTime);
		}
		
		synchronized(property)
		{
			newValues.forEach(property.getValues()::add);
			
			FoamFixCompatibility.removePropertyFromEntryMap(property);
		}
		
		synchronized(stateIdList)
		{
			for(final RefreshableStateFactory<O, S> factory : factoriesToRefresh)
			{
				allFutures.add(CompletableFuture.supplyAsync(() ->
				{
					return factory.refreshPropertyValues(property, newValues);
				},
				EXECUTOR).thenAccept(newStates::addAll));
			}
			
			CompletableFuture.allOf(allFutures.stream().toArray(CompletableFuture<?>[]::new))
			.thenAccept(v ->
			{
				newStates.forEach(state ->
				{
					newStateConsumer.accept(state);
					stateIdList.add(state);
				});
				
				if(enableDebugLogging)
				{
					LOGGER.info("Added {} new states for values(s) {} after {} ms.", newStates.size(), newValues, (System.nanoTime() - startTime) / 1_000_000);
				}
			}).join();
		}
	}
	
	public static void reorderBlockStates()
	{
		reorderStates(Registry.BLOCK, Block.STATE_IDS, Block::getStateFactory, state -> state.contains(FluidProperty.FLUID) && !state.get(FluidProperty.FLUID).equals(Registry.FLUID.getDefaultId()));
	}
	
	public static <O, V extends Comparable<V>, S extends PropertyContainer<S>> void reorderStates(final Iterable<O> registry, final IdList<S> stateIdList, final Function<O, StateFactory<O, S>> factoryGetter, final Predicate<S> deferredCondition)
	{
		@SuppressWarnings("unchecked")
		final RemovableIdList<S> removableIdList = ((RemovableIdList<S>) stateIdList);
		removableIdList.fabric_clear();
		
		final Collection<S> allStates = new LinkedList<>();
		
		for(final O entry : registry)
		{
			factoryGetter.apply(entry).getStates().forEach(allStates::add);
		}
		
		final Collection<S> deferredStates = new LinkedList<>();
		
		for(final S state : allStates)
		{
			if(deferredCondition.test(state))
			{
				deferredStates.add(state);
			}
			else
			{
				stateIdList.add(state);
			}
		}
		
		deferredStates.forEach(stateIdList::add);
	}
	
	public static Identifier id(final String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
