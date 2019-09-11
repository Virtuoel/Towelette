package virtuoel.towelette;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.property.Property;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.statement.api.StateRefresher;
import virtuoel.statement.api.StatementApi;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.ToweletteApi;
import virtuoel.towelette.api.ToweletteConfig;

public class Towelette implements ModInitializer, ToweletteApi, StatementApi
{
	public static final Logger LOGGER = LogManager.getLogger(ToweletteApi.MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(id("displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(id("undisplaceable"));
	
	public static final BiPredicate<Fluid, Identifier> ENTRYPOINT_WHITELIST_PREDICATE = (fluid, id) -> ToweletteApi.ENTRYPOINTS.stream().noneMatch(api -> api.isFluidBlacklisted(fluid, id));
	
	public static final Collection<Identifier> FLUID_ID_BLACKLIST = new HashSet<>();
	
	@Override
	public void onInitialize()
	{
		Optional.ofNullable(ToweletteConfig.DATA.get("fluidBlacklist"))
		.filter(JsonElement::isJsonArray)
		.map(JsonElement::getAsJsonArray)
		.ifPresent(array ->
		{
			array.forEach(element ->
			{
				if(element.isJsonPrimitive())
				{
					FLUID_ID_BLACKLIST.add(new Identifier(element.getAsString()));
				}
			});
		});
		
		StateRefresher.INSTANCE.refreshBlockStates(
			FluidProperty.FLUID,
			Registry.FLUID.getIds().stream()
			.filter(f -> ENTRYPOINT_WHITELIST_PREDICATE.test(Registry.FLUID.get(f), f))
			.collect(ImmutableSet.toImmutableSet())
		);
		
		RegistryEntryAddedCallback.event(Registry.FLUID).register(
			(rawId, identifier, object) ->
			{
				if(ENTRYPOINT_WHITELIST_PREDICATE.test(object, identifier))
				{
					StateRefresher.INSTANCE.refreshBlockStates(FluidProperty.FLUID, ImmutableSet.of(identifier));
				}
			}
		);
		
		RegistryIdRemapCallback.event(Registry.BLOCK).register(remapState ->
		{
			StateRefresher.INSTANCE.reorderBlockStates();
		});
	}
	
	@Override
	public boolean isFluidBlacklisted(Fluid fluid, Identifier id)
	{
		final boolean allowFlowing = Optional.ofNullable(ToweletteConfig.DATA.get("flowingFluidlogging"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(false);
		
		return FLUID_ID_BLACKLIST.contains(id) || FluidProperty.FLUID.getValues().contains(id) || (!allowFlowing && !fluid.getDefaultState().isStill());
	}
	
	@Override
	public boolean shouldDeferState(PropertyContainer<?> state)
	{
		final ImmutableMap<Property<?>, Comparable<?>> entries = state.getEntries();
		final boolean canContainFluid = entries.containsKey(FluidProperty.FLUID);
		boolean deferred = canContainFluid && !state.get(FluidProperty.FLUID).equals(Registry.FLUID.getDefaultId());
		deferred |= canContainFluid && entries.containsKey(FluidProperty.LEVEL_1_8) && state.get(FluidProperty.LEVEL_1_8) != 8;
		deferred |= canContainFluid && entries.containsKey(FluidProperty.FALLING) && state.get(FluidProperty.FALLING);
		return deferred;
	}
	
	public static Identifier id(final String name)
	{
		return new Identifier(ToweletteApi.MOD_ID, name);
	}
}
