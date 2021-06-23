package virtuoel.towelette.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import virtuoel.towelette.util.JsonConfigHandler;

public class ToweletteConfig
{
	private static final Collection<Consumer<JsonObject>> DEFAULT_VALUES = new ArrayList<>();
	
	public static final Common COMMON = new Common();
	public static final Client CLIENT = new Client();
	public static final Server SERVER = new Server();
	
	public static class Common
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
		
		Common()
		{
			this.replaceableFluids = booleanConfig("replaceableFluids", false);
			this.unpushableFluids = booleanConfig("unpushableFluids", true);
			this.flowingFluidlogging = booleanConfig("flowingFluidlogging", false);
			this.accurateFlowBlocking = booleanConfig("accurateFlowBlocking", true);
			
			this.automaticFluidlogging = booleanConfig("automaticFluidlogging", true);
			this.automaticWaterlogging = booleanConfig("automaticWaterlogging", false);
			
			this.onlyAllowWhitelistedFluids = booleanConfig("onlyAllowWhitelistedFluids", false);
			
			this.whitelistedFluidIds = stringListConfig("whitelistedFluidIds");
			this.whitelistedFluidModIds = stringListConfig("whitelistedFluidModIds");
			
			this.enableBlacklistAPI = booleanConfig("enableBlacklistAPI", true);
			
			this.blacklistedFluidIds = stringListConfig("blacklistedFluidIds");
			this.blacklistedFluidModIds = stringListConfig("blacklistedFluidModIds");
			this.addedFluidloggableBlocks = stringListConfig("addedFluidloggableBlocks");
			this.addedFlowingFluidloggableBlocks = stringListConfig("addedFlowingFluidloggableBlocks");
			this.addedWaterloggableBlocks = stringListConfig("addedWaterloggableBlocks");
			this.removedFluidloggableBlocks = stringListConfig("removedFluidloggableBlocks");
			this.removedWaterloggableBlocks = stringListConfig("removedWaterloggableBlocks");
		}
	}
	
	public static class Client
	{
		Client()
		{
			
		}
	}
	
	public static class Server
	{
		Server()
		{
			
		}
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
	public static final Supplier<JsonObject> HANDLER = createConfig(ToweletteApi.MOD_ID);
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
	public static final JsonObject DATA = HANDLER.get();
	
	private static Supplier<JsonObject> createConfig(final String namespace)
	{
		return new JsonConfigHandler(
			namespace,
			"config.json",
			() ->
			{
				final JsonObject config = new JsonObject();
				
				for (final Consumer<JsonObject> value : DEFAULT_VALUES)
				{
					value.accept(config);
				}
				
				return config;
			}
		);
	}
	
	private static Supplier<Boolean> booleanConfig(String config, boolean defaultValue)
	{
		DEFAULT_VALUES.add(c -> c.addProperty(config, defaultValue));
		
		return () -> Optional.ofNullable(DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(defaultValue);
	}
	
	private static Supplier<List<String>> stringListConfig(String config)
	{
		return listConfig(config, JsonElement::getAsString);
	}
	
	private static <T> Supplier<List<T>> listConfig(String config, Function<JsonElement, T> mapper)
	{
		DEFAULT_VALUES.add(c -> c.add(config, new JsonArray()));
		
		return () -> Optional.ofNullable(DATA.get(config))
			.filter(JsonElement::isJsonArray).map(JsonElement::getAsJsonArray)
			.map(JsonArray::spliterator).map(a -> StreamSupport.stream(a, false))
			.map(s -> s.map(mapper).collect(Collectors.toList()))
			.orElseGet(ArrayList::new);
	}
}
