package virtuoel.towelette;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.RemovableIdList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.RefreshableStateFactory;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FoamFixCompatibility;

public class Towelette implements ModInitializer
{
	public static final String MOD_ID = "towelette";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	public Towelette()
	{
		ToweletteConfig.DATA.getClass();
		
		RegistryEntryAddedCallback.event(Registry.FLUID).register(
			(rawId, identifier, object) ->
			{
				if(FluidProperty.FLUID.filter(object))
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
	public void onInitialize()
	{
		if(FluidProperty.FLUID.filter(Fluids.WATER))
		{
			refreshBlockStates(ImmutableSet.of(Registry.FLUID.getId(Fluids.WATER)));
		}
		
		if(FluidProperty.FLUID.filter(Fluids.LAVA))
		{
			refreshBlockStates(ImmutableSet.of(Registry.FLUID.getId(Fluids.LAVA)));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void refreshBlockStates(final Collection<Identifier> newIds)
	{
		newIds.forEach(FluidProperty.FLUID.getValues()::add);
		
		FoamFixCompatibility.removePropertyFromEntryMap(FluidProperty.FLUID);
		
		final Collection<BlockState> newStates = new LinkedList<>();
		
		for(final Block block : Registry.BLOCK)
		{
			if(block.getDefaultState().contains(FluidProperty.FLUID))
			{
				((RefreshableStateFactory<BlockState>) block.getStateFactory())
					.refreshPropertyValues(FluidProperty.FLUID, newIds)
					.forEach(newStates::add);
			}
		}
		
		newStates.forEach(state ->
		{
			state.initShapeCache();
			Block.STATE_IDS.add(state);
		});
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
