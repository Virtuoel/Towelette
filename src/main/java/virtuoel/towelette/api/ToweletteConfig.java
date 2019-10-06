package virtuoel.towelette.api;

import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import virtuoel.towelette.util.JsonConfigHandler;

public class ToweletteConfig
{
	public static final Supplier<JsonObject> HANDLER =
		new JsonConfigHandler(
			ToweletteApi.MOD_ID,
			ToweletteApi.MOD_ID + "/config.json",
			ToweletteConfig::createDefaultConfig
		);
	
	public static final JsonObject DATA = HANDLER.get();
	
	private static JsonObject createDefaultConfig()
	{
		final JsonObject config = new JsonObject();
		
		config.addProperty("replaceableFluids", false);
		config.addProperty("unpushableFluids", true);
		config.addProperty("flowingFluidlogging", false);
		
		final JsonObject fluidlogging = new JsonObject();
		fluidlogging.addProperty("barrier", false);
		fluidlogging.addProperty("beacon", false);
		fluidlogging.addProperty("cauldron", false);
		fluidlogging.addProperty("end_portal", false);
		fluidlogging.addProperty("farmland", false);
		fluidlogging.addProperty("fire", false);
		fluidlogging.addProperty("grass_path", false);
		fluidlogging.addProperty("leaves", false);
		fluidlogging.addProperty("lily_pad", false);
		fluidlogging.addProperty("nether_portal", false);
		fluidlogging.addProperty("observer", false);
		fluidlogging.addProperty("pistons", true);
		fluidlogging.addProperty("redstone_block", false);
		fluidlogging.addProperty("redstone_dust", true);
		fluidlogging.addProperty("shulker_boxes", false);
		fluidlogging.addProperty("slime_block", false);
		fluidlogging.addProperty("spawner", false);
		fluidlogging.addProperty("structure_void", false);
		fluidlogging.addProperty("torches", true);
		
		config.add("fluidlogging", fluidlogging);
		
		config.addProperty("enableBlacklistAPI", true);
		
		final JsonArray blacklistedFluidIds = new JsonArray();
		
		config.add("blacklistedFluidIds", blacklistedFluidIds);
		
		return config;
	}
}
