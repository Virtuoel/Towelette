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
		
		config.addProperty("onlyAllowWhitelistedFluids", false);
		
		final JsonArray whitelistedFluidIds = new JsonArray();
		config.add("whitelistedFluidIds", whitelistedFluidIds);
		
		config.addProperty("enableBlacklistAPI", true);
		
		final JsonArray blacklistedFluidIds = new JsonArray();
		config.add("blacklistedFluidIds", blacklistedFluidIds);
		
		final JsonArray addedFluidloggableBlocks = new JsonArray();
		config.add("addedFluidloggableBlocks", addedFluidloggableBlocks);
		
		final JsonArray addedFlowingFluidloggableBlocks = new JsonArray();
		config.add("addedFlowingFluidloggableBlocks", addedFlowingFluidloggableBlocks);
		
		final JsonArray addedWaterloggableBlocks = new JsonArray();
		config.add("addedWaterloggableBlocks", addedWaterloggableBlocks);
		
		final JsonArray removedFluidloggableBlocks = new JsonArray();
		config.add("removedFluidloggableBlocks", removedFluidloggableBlocks);
		
		final JsonArray removedWaterloggableBlocks = new JsonArray();
		config.add("removedWaterloggableBlocks", removedWaterloggableBlocks);
		
		return config;
	}
}
