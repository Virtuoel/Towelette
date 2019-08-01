package virtuoel.towelette;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.RefreshableStateFactory;
import virtuoel.towelette.api.FluidProperty;
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
	}
	
	@Override
	public void onInitialize()
	{
		refreshBlockStates(ImmutableSet.of(new Identifier("water"), new Identifier("lava")));
	}
	
	@SuppressWarnings("unchecked")
	public static void refreshBlockStates(Collection<Identifier> newIds)
	{
		newIds.forEach(FluidProperty.FLUID.getValues()::add);
		
		FoamFixCompatibility.PROPERTY_ENTRY_MAP.ifPresent(map ->
		{
			map.remove(FluidProperty.FLUID);
		});
		
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
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
