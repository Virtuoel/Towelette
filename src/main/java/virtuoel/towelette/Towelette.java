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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.RemovableIdList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.StateFactory;
import net.minecraft.tag.Tag;
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
		
		Registry.FLUID.stream()
		.filter(f -> ENTRYPOINT_WHITELIST_PREDICATE.test(f, Registry.FLUID.getId(f)))
		.map(Registry.FLUID::getId)
		.map(ImmutableSet::of)
		.forEach(Towelette::refreshBlockStates);
		
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
	
	@SuppressWarnings("unchecked")
	public static void refreshBlockStates(final Collection<Identifier> newIds)
	{
		final List<StateFactory<Block, BlockState>> factoriesToRefresh = new LinkedList<>();
		
		for(final Block block : Registry.BLOCK)
		{
			if(block.getDefaultState().contains(FluidProperty.FLUID))
			{
				factoriesToRefresh.add(block.getStateFactory());
			}
		}
		
		final Collection<BlockState> newStates = new ConcurrentLinkedQueue<>();
		
		final Collection<CompletableFuture<?>> allFutures = new LinkedList<>();
		
		synchronized(FluidProperty.FLUID)
		{
			newIds.forEach(FluidProperty.FLUID.getValues()::add);
			
			FoamFixCompatibility.removePropertyFromEntryMap(FluidProperty.FLUID);
		}
		
		synchronized(Block.STATE_IDS)
		{
			for(final StateFactory<Block, BlockState> factory : factoriesToRefresh)
			{
				allFutures.add(CompletableFuture.supplyAsync(() ->
				{
					return ((RefreshableStateFactory<BlockState>) factory)
						.refreshPropertyValues(FluidProperty.FLUID, newIds);
				},
				EXECUTOR).thenAccept(newStates::addAll));
			}
			
			CompletableFuture.allOf(allFutures.stream().toArray(CompletableFuture<?>[]::new))
			.thenAccept(v ->
			{
				newStates.forEach(state ->
				{
					state.initShapeCache();
					Block.STATE_IDS.add(state);
				});
			}).join();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void reorderBlockStates()
	{
		((RemovableIdList<BlockState>) Block.STATE_IDS).fabric_clear();
		
		final Collection<BlockState> allStates = new LinkedList<>();
		
		for(final Block block : Registry.BLOCK)
		{
			block.getStateFactory().getStates().forEach(allStates::add);
		}
		
		final Collection<BlockState> deferredStates = new LinkedList<>();
		
		for(final BlockState state : allStates)
		{
			if(state.contains(FluidProperty.FLUID) && !state.get(FluidProperty.FLUID).equals(Registry.FLUID.getDefaultId()))
			{
				deferredStates.add(state);
			}
			else
			{
				Block.STATE_IDS.add(state);
			}
		}
		
		deferredStates.forEach(Block.STATE_IDS::add);
	}
	
	public static Identifier id(final String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
