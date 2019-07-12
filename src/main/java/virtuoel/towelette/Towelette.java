package virtuoel.towelette;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.RemovableIdList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.tag.Tag;
import net.minecraft.util.IdList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.StateFactoryRebuildable;
import virtuoel.towelette.util.FoamFixCompatibility;

public class Towelette implements ModInitializer
{
	public static final String MOD_ID = "towelette";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	public Towelette()
	{
		refreshBlockStates(true);
		RegistryEntryAddedCallback.event(Registry.FLUID).register(
			(rawId, identifier, object) ->
			{
				if(FluidProperty.FLUID.filter(object))
				{
					refreshBlockStates(true);
				}
			}
		);
		RegistryIdRemapCallback.event(Registry.BLOCK).register(s -> refreshBlockStates(false));
	}
	
	public static void refreshBlockStates(boolean rebuild)
	{
		refreshStates(
			!rebuild ? Optional.empty() : Optional.of(() ->
			{
				FluidProperty.FLUID.getValues().clear();
				
				FoamFixCompatibility.PROPERTY_ENTRY_MAP.ifPresent(map ->
				{
					map.remove(FluidProperty.FLUID);
				});
			}),
			Registry.BLOCK,
			!rebuild ? Optional.empty() : Optional.of(block ->
			{
				if(block.getDefaultState().contains(FluidProperty.FLUID))
				{
					((StateFactoryRebuildable) block).rebuildStates();
				}
			}),
			block -> block.getStateFactory().getStates(),
			!rebuild ? Optional.empty() : Optional.of(BlockState::initShapeCache),
			Block.STATE_IDS,
			Optional.of(state -> !state.contains(FluidProperty.FLUID) || state.get(FluidProperty.FLUID).equals(Registry.FLUID.getDefaultId()))
		);
	}
	
	public static <T, O, S, U extends AbstractPropertyContainer<O, S>> void refreshStates(Optional<Runnable> preIterationTask, Iterable<T> collection, Optional<Consumer<T>> iterationTask, Function<T, Collection<U>> stateGetter, Optional<Consumer<U>> stateConsumer, IdList<U> immediateStateCollection, Optional<Predicate<U>> immediateStateCondition)
	{
		((RemovableIdList<?>) immediateStateCollection).fabric_clear();
		
		preIterationTask.ifPresent(Runnable::run);
		
		final List<U> deferredStates = new LinkedList<>();
		
		for(final T object : collection)
		{
			iterationTask.ifPresent(c -> c.accept(object));
			
			stateGetter.apply(object).forEach(state ->
			{
				stateConsumer.ifPresent(c -> c.accept(state));
				
				if(immediateStateCondition.map(p -> p.test(state)).orElse(true))
				{
					immediateStateCollection.add(state);
				}
				else
				{
					deferredStates.add(state);
				}
			});
		}
		
		deferredStates.forEach(immediateStateCollection::add);
	}
	
	@Override
	public void onInitialize()
	{
		
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
