package virtuoel.towelette.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import virtuoel.kanos_config.api.JsonConfigBuilder;
import virtuoel.towelette.Towelette;
import virtuoel.towelette.util.BackwardsCompatibility;
import virtuoel.towelette.util.ModContainerUtils;

public class ToweletteConfig
{
	@ApiStatus.Internal
	public static final JsonConfigBuilder BUILDER = new JsonConfigBuilder(
		Towelette.MOD_ID,
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
		public final Supplier<Boolean> onlyAcceptAllowedFluids;
		public final Supplier<List<String>> allowedFluidIds;
		public final Supplier<List<String>> allowedFluidModIds;
		public final Supplier<List<String>> deniedFluidIds;
		public final Supplier<List<String>> deniedFluidModIds;
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
					if (ModContainerUtils.isModUserAdded(Towelette.MOD_ID))
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
			
			this.onlyAcceptAllowedFluids = builder.booleanConfig("onlyAcceptAllowedFluids", false);
			
			this.allowedFluidIds = builder.stringListConfig("allowedFluidIds");
			this.allowedFluidModIds = builder.stringListConfig("allowedFluidModIds");
			
			this.deniedFluidIds = builder.stringListConfig("deniedFluidIds");
			this.deniedFluidModIds = builder.stringListConfig("deniedFluidModIds");
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
	
	private ToweletteConfig()
	{
		
	}
	
	static
	{
		BackwardsCompatibility.populateFieldsIfNeeded(BUILDER);
	}
}
