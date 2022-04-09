package virtuoel.towelette.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import virtuoel.kanos_config.api.JsonConfigBuilder;
import virtuoel.towelette.util.ModContainerUtils;

public class ToweletteConfig
{
	@ApiStatus.Internal
	public static final JsonConfigBuilder BUILDER = new JsonConfigBuilder(
		ToweletteApi.MOD_ID,
		"config.json"
	);
	
	public static final Client CLIENT = new Client(BUILDER);
	public static final Common COMMON = new Common(BUILDER);
	public static final Server SERVER = new Server(BUILDER);
	
	public static final class Common
	{
		public final Supplier<Boolean> replaceableFluids;
		public final Supplier<Boolean> unpushableFluids;
		public final Supplier<Boolean> flowingFluidlogging;
		public final Supplier<Boolean> accurateFlowBlocking;
		public final Supplier<Boolean> automaticFluidlogging;
		public final Supplier<Boolean> automaticWaterlogging;
		public final Supplier<Boolean> onlyAllowWhitelistedFluids;
		public final Supplier<List<String>> whitelistedFluidIds;
		public final Supplier<List<String>> whitelistedFluidModIds;
		public final Supplier<Boolean> enableBlacklistAPI;
		public final Supplier<List<String>> blacklistedFluidIds;
		public final Supplier<List<String>> blacklistedFluidModIds;
		public final Supplier<List<String>> addedFluidloggableBlocks;
		public final Supplier<List<String>> addedFlowingFluidloggableBlocks;
		public final Supplier<List<String>> addedWaterloggableBlocks;
		public final Supplier<List<String>> removedFluidloggableBlocks;
		public final Supplier<List<String>> removedWaterloggableBlocks;
		
		private Common(final JsonConfigBuilder builder)
		{
			this.replaceableFluids = builder.booleanConfig("replaceableFluids", false);
			this.unpushableFluids = builder.booleanConfig("unpushableFluids", true);
			this.flowingFluidlogging = builder.booleanConfig("flowingFluidlogging", false);
			this.accurateFlowBlocking = builder.booleanConfig("accurateFlowBlocking", true);
			
			this.automaticFluidlogging = builder.customConfig(
				"automaticFluidlogging",
				config -> v ->
				{
					if (ModContainerUtils.isModUserAdded(ToweletteApi.MOD_ID))
					{
						config.get().addProperty("automaticFluidlogging", v);
					}
				},
				true,
				config -> () -> Optional.ofNullable(config.get().get("automaticFluidlogging"))
					.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
					.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
					.orElse(false)
			);
			this.automaticWaterlogging = builder.booleanConfig("automaticWaterlogging", false);
			
			this.onlyAllowWhitelistedFluids = builder.booleanConfig("onlyAllowWhitelistedFluids", false);
			
			this.whitelistedFluidIds = builder.stringListConfig("whitelistedFluidIds");
			this.whitelistedFluidModIds = builder.stringListConfig("whitelistedFluidModIds");
			
			this.enableBlacklistAPI = builder.booleanConfig("enableBlacklistAPI", true);
			
			this.blacklistedFluidIds = builder.stringListConfig("blacklistedFluidIds");
			this.blacklistedFluidModIds = builder.stringListConfig("blacklistedFluidModIds");
			this.addedFluidloggableBlocks = builder.stringListConfig("addedFluidloggableBlocks");
			this.addedFlowingFluidloggableBlocks = builder.stringListConfig("addedFlowingFluidloggableBlocks");
			this.addedWaterloggableBlocks = builder.stringListConfig("addedWaterloggableBlocks");
			this.removedFluidloggableBlocks = builder.stringListConfig("removedFluidloggableBlocks");
			this.removedWaterloggableBlocks = builder.stringListConfig("removedWaterloggableBlocks");
		}
	}
	
	public static final class Client
	{
		private Client(final JsonConfigBuilder builder)
		{
			
		}
	}
	
	public static final class Server
	{
		private Server(final JsonConfigBuilder builder)
		{
			
		}
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
	public static final Supplier<com.google.gson.JsonObject> HANDLER = BUILDER.config;
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
	public static final com.google.gson.JsonObject DATA = BUILDER.config.get();
	
	private ToweletteConfig()
	{
		
	}
}
