package virtuoel.towelette;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IdList;
import virtuoel.statement.api.StateRefresher;
import virtuoel.statement.api.StatementApi;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.init.TagRegistrar;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.RegistryUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

public class Towelette implements ModInitializer, StatementApi
{
	public static final String MOD_ID = "towelette";
	
	public static final ILogger LOGGER = MixinService.getService().getLogger(MOD_ID);
	
	public static final Collection<Identifier> ALLOWED_FLUID_IDS = new HashSet<>();
	public static final Collection<String> ALLOWED_FLUID_MOD_IDS = new HashSet<>();
	public static final Collection<Identifier> DENIED_FLUID_IDS = new HashSet<>();
	public static final Collection<String> DENIED_FLUID_MOD_IDS = new HashSet<>();
	
	public static final Collection<Identifier> FLUIDLOGGABLE_ADDITIONS = new HashSet<>();
	public static final Collection<Identifier> FLOWING_FLUIDLOGGABLE_ADDITIONS = new HashSet<>();
	public static final Collection<Identifier> FLUIDLOGGABLE_REMOVALS = new HashSet<>();
	public static final Collection<Identifier> WATERLOGGABLE_ADDITIONS = new HashSet<>();
	public static final Collection<Identifier> WATERLOGGABLE_REMOVALS = new HashSet<>();
	
	public Towelette()
	{
		ToweletteConfig.BUILDER.config.get();
	}
	
	@Override
	public void onInitialize()
	{
		FabricLoader.getInstance().getObjectShare().putIfAbsent(
			id("allowed_fluid_ids").toString(),
			ALLOWED_FLUID_IDS
		);
		
		FabricLoader.getInstance().getObjectShare().putIfAbsent(
			id("allowed_fluid_mod_ids").toString(),
			ALLOWED_FLUID_MOD_IDS
		);
		
		FabricLoader.getInstance().getObjectShare().putIfAbsent(
			id("denied_fluid_ids").toString(),
			DENIED_FLUID_IDS
		);
		
		FabricLoader.getInstance().getObjectShare().putIfAbsent(
			id("denied_fluid_mod_ids").toString(),
			DENIED_FLUID_MOD_IDS
		);
		
		TagRegistrar.DISPLACEABLE.getClass();
		
		ALLOWED_FLUID_IDS.addAll(configIdArray(ToweletteConfig.COMMON.allowedFluidIds));
		ALLOWED_FLUID_MOD_IDS.addAll(ToweletteConfig.COMMON.allowedFluidModIds.get());
		
		DENIED_FLUID_IDS.addAll(configIdArray(ToweletteConfig.COMMON.deniedFluidIds));
		DENIED_FLUID_MOD_IDS.addAll(ToweletteConfig.COMMON.deniedFluidModIds.get());
		
		FLUIDLOGGABLE_ADDITIONS.addAll(configIdArray(ToweletteConfig.COMMON.addedFluidloggableBlocks));
		FLOWING_FLUIDLOGGABLE_ADDITIONS.addAll(configIdArray(ToweletteConfig.COMMON.addedFlowingFluidloggableBlocks));
		FLUIDLOGGABLE_REMOVALS.addAll(configIdArray(ToweletteConfig.COMMON.removedFluidloggableBlocks));
		
		WATERLOGGABLE_ADDITIONS.addAll(configIdArray(ToweletteConfig.COMMON.addedWaterloggableBlocks));
		WATERLOGGABLE_REMOVALS.addAll(configIdArray(ToweletteConfig.COMMON.removedWaterloggableBlocks));
		
		FLUIDLOGGABLE_ADDITIONS.removeAll(FLUIDLOGGABLE_REMOVALS);
		FLOWING_FLUIDLOGGABLE_ADDITIONS.removeAll(FLUIDLOGGABLE_REMOVALS);
		FLUIDLOGGABLE_ADDITIONS.removeAll(FLOWING_FLUIDLOGGABLE_ADDITIONS);
		WATERLOGGABLE_ADDITIONS.removeAll(WATERLOGGABLE_REMOVALS);
		
		final boolean[] changedStates = { false };
		
		final boolean automaticFluidlogging = ToweletteConfig.COMMON.automaticFluidlogging.get();
		final boolean automaticWaterlogging = ToweletteConfig.COMMON.automaticWaterlogging.get();
		
		if (automaticFluidlogging || automaticWaterlogging)
		{
			final boolean flowingFluidlogging = ToweletteConfig.COMMON.flowingFluidlogging.get();
			
			final Collection<Block> fluidloggableDefaults = new LinkedList<>();
			final Collection<Block> waterloggableDefaults = new LinkedList<>();
			
			for (final Identifier id : RegistryUtils.getIds(RegistryUtils.BLOCK_REGISTRY))
			{
				final Block block = RegistryUtils.get(RegistryUtils.BLOCK_REGISTRY, id);
				
				if (automaticFluidlogging && !FLUIDLOGGABLE_REMOVALS.contains(id))
				{
					fluidloggableDefaults.add(block);
				}
				
				if (automaticWaterlogging && !WATERLOGGABLE_REMOVALS.contains(id))
				{
					waterloggableDefaults.add(block);
				}
			}
			
			for (final Block block : fluidloggableDefaults)
			{
				if (AutomaticFluidloggableMarker.shouldAddProperties(block))
				{
					addFluidProperties(block, flowingFluidlogging);
				}
			}
			
			for (final Block block : waterloggableDefaults)
			{
				if (AutomaticWaterloggableMarker.shouldAddProperties(block))
				{
					StateRefresher.INSTANCE.addBlockProperty(block, Properties.WATERLOGGED, false);
				}
			}
			
			changedStates[0] = !fluidloggableDefaults.isEmpty() || !waterloggableDefaults.isEmpty();
		}
		
		for (final Identifier id : FLUIDLOGGABLE_ADDITIONS)
		{
			RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, id).ifPresent(block ->
			{
				StateRefresher.INSTANCE.addBlockProperty(
					block,
					FluidProperties.FLUID,
					RegistryUtils.getDefaultId(RegistryUtils.FLUID_REGISTRY)
				);
				
				changedStates[0] = true;
			});
		}
		
		for (final Identifier id : FLOWING_FLUIDLOGGABLE_ADDITIONS)
		{
			RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, id).ifPresent(block ->
			{
				addFluidProperties(block, true);
				
				changedStates[0] = true;
			});
		}
		
		for (final Identifier id : FLUIDLOGGABLE_REMOVALS)
		{
			RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, id).ifPresent(block ->
			{
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, FluidProperties.FLUID).isEmpty();
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, FluidProperties.LEVEL_1_8).isEmpty();
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, FluidProperties.FALLING).isEmpty();
			});
		}
		
		for (final Identifier id : WATERLOGGABLE_ADDITIONS)
		{
			RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, id).ifPresent(block ->
			{
				StateRefresher.INSTANCE.addBlockProperty(block, Properties.WATERLOGGED, false);
				
				changedStates[0] = true;
			});
		}
		
		for (final Identifier id : WATERLOGGABLE_REMOVALS)
		{
			RegistryUtils.getOrEmpty(RegistryUtils.BLOCK_REGISTRY, id).ifPresent(block ->
			{
				changedStates[0] |= !StateRefresher.INSTANCE.removeBlockProperty(block, Properties.WATERLOGGED).isEmpty();
			});
		}
		
		RegistryEntryAddedCallback.event(RegistryUtils.BLOCK_REGISTRY).register((rawId, identifier, object) ->
		{
			boolean didChange = false;
			
			if (WATERLOGGABLE_REMOVALS.contains(identifier))
			{
				didChange |= !StateRefresher.INSTANCE.removeBlockProperty(object, Properties.WATERLOGGED).isEmpty();
			}
			else if (WATERLOGGABLE_ADDITIONS.contains(identifier) || (ToweletteConfig.COMMON.automaticWaterlogging.get() && AutomaticWaterloggableMarker.shouldAddProperties(object)))
			{
				StateRefresher.INSTANCE.addBlockProperty(object, Properties.WATERLOGGED, false);
				
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
					addFluidProperties(object, flowing);
					
					didChange = true;
				}
				else if (ToweletteConfig.COMMON.automaticFluidlogging.get() && AutomaticFluidloggableMarker.shouldAddProperties(object))
				{
					addFluidProperties(object, ToweletteConfig.COMMON.flowingFluidlogging.get());
					
					didChange = true;
				}
			}
			
			if (didChange)
			{
				StateRefresher.INSTANCE.reorderBlockStates();
			}
		});
		
		StateRefresher.INSTANCE.refreshBlockStates(
			FluidProperties.FLUID,
			RegistryUtils.getIds(RegistryUtils.FLUID_REGISTRY).stream()
			.filter(f -> filterFluid(RegistryUtils.get(RegistryUtils.FLUID_REGISTRY, f), f))
			.collect(ImmutableSet.toImmutableSet()),
			ImmutableSet.of()
		);
		
		if (changedStates[0])
		{
			StateRefresher.INSTANCE.reorderBlockStates();
		}
		
		RegistryEntryAddedCallback.event(RegistryUtils.FLUID_REGISTRY).register((rawId, identifier, object) ->
		{
			if (filterFluid(object, identifier))
			{
				StateRefresher.INSTANCE.refreshBlockStates(FluidProperties.FLUID, ImmutableSet.of(identifier), ImmutableSet.of());
			}
		});
	}
	
	private static boolean filterFluid(final Fluid fluid, final Identifier id)
	{
		if (ALLOWED_FLUID_MOD_IDS.contains(id.getNamespace()) || ALLOWED_FLUID_IDS.contains(id))
		{
			return !FluidUtils.propertyContains(id);
		}
		
		if (ToweletteConfig.COMMON.onlyAcceptAllowedFluids.get())
		{
			return false;
		}
		
		return !(DENIED_FLUID_MOD_IDS.contains(id.getNamespace()) || DENIED_FLUID_IDS.contains(id) || FluidUtils.propertyContains(id) || (!ToweletteConfig.COMMON.flowingFluidlogging.get() && !((ToweletteFluidStateExtensions) (Object) fluid.getDefaultState()).towelette_isStill()));
	}
	
	private static List<Identifier> configIdArray(Supplier<List<String>> config)
	{
		return config.get().stream().map(Identifier::new).collect(Collectors.toList());
	}
	
	private static void addFluidProperties(Block block, boolean flowing)
	{
		StateRefresher.INSTANCE.addBlockProperty(
			block,
			FluidProperties.FLUID,
			RegistryUtils.getDefaultId(RegistryUtils.FLUID_REGISTRY)
		);
		
		if (flowing)
		{
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
		}
	}
	
	@Override
	public <S> boolean shouldDeferState(IdList<S> idList, S state)
	{
		if (idList == Block.STATE_IDS)
		{
			final ToweletteBlockStateExtensions blockState = ((ToweletteBlockStateExtensions) state);
			final ImmutableMap<?, Comparable<?>> entries = blockState.towelette_getEntries();
			
			if (entries.containsKey(FluidProperties.FLUID))
			{
				return (!blockState.towelette_get(FluidProperties.FLUID).equals(RegistryUtils.getDefaultId(RegistryUtils.FLUID_REGISTRY))) ||
					(entries.containsKey(FluidProperties.LEVEL_1_8) && blockState.<Integer>towelette_get(FluidProperties.LEVEL_1_8) != 8) ||
					(entries.containsKey(FluidProperties.FALLING) && blockState.<Boolean>towelette_get(FluidProperties.FALLING));
			}
		}
		
		return false;
	}
	
	public static Identifier id(final String name)
	{
		return new Identifier(MOD_ID, name);
	}
	
	public static Identifier id(String path, String... paths)
	{
		return id(paths.length == 0 ? path : path + "/" + String.join("/", paths));
	}
}
