package virtuoel.towelette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.RemovableIdList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
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
		rebuildStates();
		RegistryEntryAddedCallback.event(Registry.FLUID).register(
			(rawId, identifier, object) ->
			{
				if(FluidProperty.FLUID.filter(object))
				{
					rebuildStates();
				}
			}
		);
	}
	
	@SuppressWarnings("unchecked")
	public static void rebuildStates()
	{
		((RemovableIdList<BlockState>) Block.STATE_IDS).fabric_clear();
		
		FluidProperty.FLUID.getValues().clear();
		
		FoamFixCompatibility.PROPERTY_ENTRY_MAP.ifPresent(map ->
		{
			map.remove(FluidProperty.FLUID);
		});
		
		final Identifier empty = Registry.FLUID.getId(Fluids.EMPTY);
		final IdList<BlockState> fluidloggedIds = new IdList<>();
		
		for(Block block : Registry.BLOCK)
		{
			if(block.getDefaultState().contains(FluidProperty.FLUID))
			{
				((StateFactoryRebuildable) block).rebuildStates();
			}
			
			for(final BlockState state : block.getStateFactory().getStates())
			{
				state.initShapeCache();
				
				if(!state.contains(FluidProperty.FLUID) || state.get(FluidProperty.FLUID).equals(empty))
				{
					Block.STATE_IDS.add(state);
				}
				else
				{
					fluidloggedIds.add(state);
				}
			}
		}
		
		for(final BlockState state : fluidloggedIds)
		{
			Block.STATE_IDS.add(state);
		}
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
