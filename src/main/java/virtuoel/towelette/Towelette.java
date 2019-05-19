package virtuoel.towelette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.impl.registry.ListenableRegistry;
import net.fabricmc.fabric.impl.registry.RemovableIdList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.StateFactoryRebuildable;
import virtuoel.towelette.util.ConfigHandler;
import virtuoel.towelette.util.JsonConfigHandler;

public class Towelette implements ModInitializer
{
	public static final String MOD_ID = "towelette";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final ConfigHandler<JsonObject> CONFIG_HANDLER = new JsonConfigHandler(MOD_ID, MOD_ID + "/config", Towelette::createDefaultConfig);
	public static final JsonObject CONFIG = CONFIG_HANDLER.get();
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	@SuppressWarnings("unchecked")
	public Towelette()
	{
		if(CONFIG.get("rebuildStatesOnFluidRegistration").getAsBoolean())
		{
			((ListenableRegistry<Fluid>) Registry.FLUID).getPostRegisterEvent().register(
				(rawId, identifier, object) ->
				{
					if(FluidProperty.FLUID.filter(object))
					{
						((RemovableIdList<BlockState>) Block.STATE_IDS).clear();
						
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
			);
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
	
	private static JsonObject createDefaultConfig()
	{
		JsonObject config = new JsonObject();
		config.addProperty("rebuildStatesOnFluidRegistration", false);
		return config;
	}
}
