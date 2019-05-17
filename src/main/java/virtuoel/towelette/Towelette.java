package virtuoel.towelette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.ExtendedIdList;
import net.fabricmc.fabric.impl.registry.ListenableRegistry;
import net.fabricmc.fabric.impl.registry.RegistryListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.StateFactoryRebuildable;

public class Towelette implements ModInitializer
{
	public static final String MOD_ID = "towelette";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	public static boolean rebuildStatesOnFluidRegistration = true;
	
	@SuppressWarnings("unchecked")
	public Towelette()
	{
		((ListenableRegistry<Fluid>) Registry.FLUID).registerListener(new RegistryListener<Fluid>()
		{
			@Override
			public void afterRegistryRegistration(Registry<Fluid> registry, int id, Identifier identifier, Fluid object)
			{
				if(rebuildStatesOnFluidRegistration && FluidProperty.FLUID.filter(object))
				{
					((ExtendedIdList) Block.STATE_IDS).clear();
					
					FluidProperty.FLUID.getValues().clear();
					
					for(Block block : Registry.BLOCK)
					{
						if(block.getDefaultState().contains(FluidProperty.FLUID))
						{
							((StateFactoryRebuildable) block).rebuildStates();
						}
						
						for(BlockState state : block.getStateFactory().getStates())
						{
							state.initShapeCache();
							Block.STATE_IDS.add(state);
						}
					}
				}
			}
		});
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
