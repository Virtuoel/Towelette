package virtuoel.towelette;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IdList;
import net.minecraft.util.registry.Registry;
import virtuoel.statement.api.StateRefresher;
import virtuoel.statement.api.StatementApi;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.ToweletteApi;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;

public class Towelette implements ModInitializer, ToweletteApi, StatementApi
{
	public static final String MOD_ID = ToweletteApi.MOD_ID;
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	public static final Collection<Identifier> FLUID_ID_WHITELIST = new HashSet<>();
	public static final Collection<Identifier> FLUID_ID_BLACKLIST = new HashSet<>();
	
	public static final Collection<Identifier> FLUIDLOGGABLE_ADDITIONS = new HashSet<>();
	public static final Collection<Identifier> FLOWING_FLUIDLOGGABLE_ADDITIONS = new HashSet<>();
	public static final Collection<Identifier> FLUIDLOGGABLE_REMOVALS = new HashSet<>();
	public static final Collection<Identifier> WATERLOGGABLE_ADDITIONS = new HashSet<>();
	public static final Collection<Identifier> WATERLOGGABLE_REMOVALS = new HashSet<>();
	
	@Override
	public void onInitialize()
	{
		final BiConsumer<String, Collection<Identifier>> configIdCollectAction = (config, collection) ->
		{
			Optional.ofNullable(ToweletteConfig.DATA.get(config))
				.filter(JsonElement::isJsonArray)
				.map(JsonElement::getAsJsonArray)
				.ifPresent(array ->
				{
					array.forEach(element ->
					{
						if (element.isJsonPrimitive())
						{
							collection.add(new Identifier(element.getAsString()));
						}
					});
				});
		};
		
		configIdCollectAction.accept("whitelistedFluidIds", FLUID_ID_WHITELIST);
		
		configIdCollectAction.accept("blacklistedFluidIds", FLUID_ID_BLACKLIST);
		
		configIdCollectAction.accept("addedFluidloggableBlocks", FLUIDLOGGABLE_ADDITIONS);
		configIdCollectAction.accept("addedFlowingFluidloggableBlocks", FLOWING_FLUIDLOGGABLE_ADDITIONS);
		configIdCollectAction.accept("removedFluidloggableBlocks", FLUIDLOGGABLE_REMOVALS);
		
		configIdCollectAction.accept("addedWaterloggableBlocks", WATERLOGGABLE_ADDITIONS);
		configIdCollectAction.accept("removedWaterloggableBlocks", WATERLOGGABLE_REMOVALS);
		
		FLUIDLOGGABLE_ADDITIONS.removeAll(FLUIDLOGGABLE_REMOVALS);
		FLOWING_FLUIDLOGGABLE_ADDITIONS.removeAll(FLUIDLOGGABLE_REMOVALS);
		FLUIDLOGGABLE_ADDITIONS.removeAll(FLOWING_FLUIDLOGGABLE_ADDITIONS);
		WATERLOGGABLE_ADDITIONS.removeAll(WATERLOGGABLE_REMOVALS);
		
		final boolean[] changedStates = { false };
		
		for (final Identifier id : FLUIDLOGGABLE_ADDITIONS)
		{
			Registry.BLOCK.getOrEmpty(id).ifPresent(block ->
			{
				StateRefresher.INSTANCE.addBlockProperty(
					block,
					FluidProperties.FLUID,
					Registry.FLUID.getDefaultId()
				);
				
				changedStates[0] = true;
			});
		}
		
		for (final Identifier id : FLOWING_FLUIDLOGGABLE_ADDITIONS)
		{
			Registry.BLOCK.getOrEmpty(id).ifPresent(block ->
			{
				StateRefresher.INSTANCE.addBlockProperty(
					block,
					FluidProperties.FLUID,
					Registry.FLUID.getDefaultId()
				);
				
				StateRefresher.INSTANCE.addBlockProperty(
					block,
					FluidProperties.LEVEL_1_8,
					8
				);
				
				StateRefresher.INSTANCE.addBlockProperty(
					block,
					FluidProperties.FALLING,
					false
				);
				
				changedStates[0] = true;
			});
		}
		
		for (final Identifier id : FLUIDLOGGABLE_REMOVALS)
		{
			Registry.BLOCK.getOrEmpty(id).ifPresent(block ->
			{
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, FluidProperties.FLUID).isEmpty();
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, FluidProperties.LEVEL_1_8).isEmpty();
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, FluidProperties.FALLING).isEmpty();
			});
		}
		
		for (final Identifier id : WATERLOGGABLE_ADDITIONS)
		{
			Registry.BLOCK.getOrEmpty(id).ifPresent(block ->
			{
				StateRefresher.INSTANCE.addBlockProperty(
					block,
					Properties.WATERLOGGED,
					false
				);
				
				changedStates[0] = true;
			});
		}
		
		for (final Identifier id : WATERLOGGABLE_REMOVALS)
		{
			Registry.BLOCK.getOrEmpty(id).ifPresent(block ->
			{
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, Properties.WATERLOGGED).isEmpty();
			});
		}
		
		RegistryEntryAddedCallback.event(Registry.BLOCK).register(
			(rawId, identifier, object) ->
			{
				boolean didChange = false;
				
				if (WATERLOGGABLE_REMOVALS.contains(identifier))
				{
					didChange |= !StateRefresher.INSTANCE.removeBlockProperty(object, Properties.WATERLOGGED).isEmpty();
				}
				else if (WATERLOGGABLE_ADDITIONS.contains(identifier))
				{
					StateRefresher.INSTANCE.addBlockProperty(
						object,
						Properties.WATERLOGGED,
						false
					);
					
					didChange = true;
				}
				
				if (FLUIDLOGGABLE_REMOVALS.contains(identifier))
				{
					didChange |= !StateRefresher.INSTANCE.removeBlockProperty(object, FluidProperties.FLUID).isEmpty();
					didChange |= !StateRefresher.INSTANCE.removeBlockProperty(object, FluidProperties.LEVEL_1_8).isEmpty();
					didChange |= !StateRefresher.INSTANCE.removeBlockProperty(object, FluidProperties.FALLING).isEmpty();
				}
				else
				{
					final boolean flowing = FLOWING_FLUIDLOGGABLE_ADDITIONS.contains(identifier);
					
					if (flowing || FLUIDLOGGABLE_ADDITIONS.contains(identifier))
					{
						StateRefresher.INSTANCE.addBlockProperty(
							object,
							FluidProperties.FLUID,
							Registry.FLUID.getDefaultId()
						);
						
						if (flowing)
						{
							StateRefresher.INSTANCE.addBlockProperty(
								object,
								FluidProperties.LEVEL_1_8,
								8
							);
							
							StateRefresher.INSTANCE.addBlockProperty(
								object,
								FluidProperties.FALLING,
								false
							);
						}
						
						didChange = true;
					}
				}
				
				if (didChange)
				{
					StateRefresher.INSTANCE.reorderBlockStates();
				}
			}
		);
		
		StateRefresher.INSTANCE.refreshBlockStates(
			FluidProperties.FLUID,
			Registry.FLUID.getIds().stream()
			.filter(f -> filterFluid(Registry.FLUID.get(f), f, this::isFluidBlacklisted))
			.collect(ImmutableSet.toImmutableSet()),
			ImmutableSet.of()
		);
		
		if (changedStates[0])
		{
			StateRefresher.INSTANCE.reorderBlockStates();
		}
		
		RegistryEntryAddedCallback.event(Registry.FLUID).register(
			(rawId, identifier, object) ->
			{
				if (filterFluid(object, identifier, this::isFluidBlacklisted))
				{
					StateRefresher.INSTANCE.refreshBlockStates(FluidProperties.FLUID, ImmutableSet.of(identifier), ImmutableSet.of());
				}
			}
		);
	}
	
	private static boolean filterFluid(final Fluid fluid, final Identifier id, final BiPredicate<Fluid, Identifier> defaultPredicate)
	{
		if (FLUID_ID_WHITELIST.contains(id))
		{
			return !FluidUtils.propertyContains(id);
		}
		
		final boolean onlyAllowWhitelistedFluids = Optional.ofNullable(ToweletteConfig.DATA.get("onlyAllowWhitelistedFluids"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(false);
		
		if (onlyAllowWhitelistedFluids)
		{
			return false;
		}
		
		final boolean entrypointBlacklists = Optional.ofNullable(ToweletteConfig.DATA.get("enableBlacklistAPI"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true);
		
		return entrypointBlacklists ? ToweletteApi.ENTRYPOINTS.stream().noneMatch(api -> api.isFluidBlacklisted(fluid, id)) : defaultPredicate.test(fluid, id);
	}
	
	@Override
	public boolean isFluidBlacklisted(Fluid fluid, Identifier id)
	{
		if (FLUID_ID_WHITELIST.contains(id))
		{
			return FluidUtils.propertyContains(id);
		}
		
		final boolean onlyAllowWhitelistedFluids = Optional.ofNullable(ToweletteConfig.DATA.get("onlyAllowWhitelistedFluids"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(false);
		
		if (onlyAllowWhitelistedFluids)
		{
			return true;
		}
		
		final boolean allowFlowing = Optional.ofNullable(ToweletteConfig.DATA.get("flowingFluidlogging"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(false);
		
		return FLUID_ID_BLACKLIST.contains(id) || FluidUtils.propertyContains(id) || (!allowFlowing && !fluid.getDefaultState().isStill());
	}
	
	@Override
	public <S> boolean shouldDeferState(IdList<S> idList, S state)
	{
		if (idList == Block.STATE_IDS)
		{
			final BlockState blockState = ((BlockState) state);
			final ImmutableMap<Property<?>, Comparable<?>> entries = blockState.getEntries();
			
			if (entries.containsKey(FluidProperties.FLUID))
			{
				return (!blockState.get(FluidProperties.FLUID).equals(Registry.FLUID.getDefaultId())) ||
					(entries.containsKey(FluidProperties.LEVEL_1_8) && blockState.get(FluidProperties.LEVEL_1_8) != 8) ||
					(entries.containsKey(FluidProperties.FALLING) && blockState.get(FluidProperties.FALLING));
			}
		}
		
		return false;
	}
	
	public static Identifier id(final String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
