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
		config.addProperty("accurateFlowBlocking", true);
		
		// config.addProperty("automaticFluidlogging", true);
		
		final JsonObject miscFluidlogging = new JsonObject();
		miscFluidlogging.addProperty("barrier", false);
		miscFluidlogging.addProperty("beacon", false);
		miscFluidlogging.addProperty("cauldron", false);
		miscFluidlogging.addProperty("end_portal", false);
		miscFluidlogging.addProperty("farmland", false);
		miscFluidlogging.addProperty("fire", false);
		miscFluidlogging.addProperty("grass_path", false);
		miscFluidlogging.addProperty("honey_block", false);
		miscFluidlogging.addProperty("leaves", false);
		miscFluidlogging.addProperty("lily_pad", false);
		miscFluidlogging.addProperty("nether_portal", false);
		miscFluidlogging.addProperty("observer", false);
		miscFluidlogging.addProperty("pistons", true);
		miscFluidlogging.addProperty("redstone_block", false);
		miscFluidlogging.addProperty("redstone_dust", true);
		miscFluidlogging.addProperty("shulker_boxes", false);
		miscFluidlogging.addProperty("slime_block", false);
		miscFluidlogging.addProperty("spawner", false);
		miscFluidlogging.addProperty("structure_void", false);
		miscFluidlogging.addProperty("torches", true);
		
		config.add("miscFluidlogging", miscFluidlogging);
		
		config.addProperty("onlyAllowWhitelistedFluids", false);
		
		final JsonArray whitelistedFluidIds = new JsonArray();
		config.add("whitelistedFluidIds", whitelistedFluidIds);
		
		config.addProperty("enableBlacklistAPI", true);
		
		final JsonArray blacklistedFluidIds = new JsonArray();
		config.add("blacklistedFluidIds", blacklistedFluidIds);
		
		final JsonArray addedFluidloggableBlocks = new JsonArray();
		config.add("addedFluidloggableBlocks", addedFluidloggableBlocks);
		
		final JsonArray removedFluidloggableBlocks = new JsonArray();
		config.add("removedFluidloggableBlocks", removedFluidloggableBlocks);
		
		return config;
	}
}
