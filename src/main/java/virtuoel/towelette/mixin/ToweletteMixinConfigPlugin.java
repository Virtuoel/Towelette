package virtuoel.towelette.mixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.VersionData;

public class ToweletteMixinConfigPlugin implements IMixinConfigPlugin
{
	private static final String MIXIN_PACKAGE = "virtuoel.towelette.mixin";
	private static final String FLUIDLOGGABLE_PACKAGE = MIXIN_PACKAGE + ".fluidloggable";
	
	@Override
	public void onLoad(String mixinPackage)
	{
		if (!mixinPackage.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package: Expected \"%s\", but found \"%s\".", MIXIN_PACKAGE, mixinPackage)
			);
		}
	}
	
	@Override
	public String getRefMapperConfig()
	{
		return null;
	}
	
	private static boolean isConfigEnabled(final Pair<String, Boolean> data)
	{
		return Optional.ofNullable(ToweletteConfig.DATA.get("miscFluidlogging"))
			.filter(JsonElement::isJsonObject)
			.map(JsonElement::getAsJsonObject)
			.map(o -> o.get(data.getLeft()))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean)
			.map(JsonElement::getAsBoolean)
			.orElse(data.getRight());
	}
	
	private static final Map<String, Pair<String, Boolean>> CLASS_CONFIG_MAP = buildMap();
	
	private static void addConfig(final Map<String, Pair<String, Boolean>> map, final String mixinClassName, final String config, final boolean defaultValue)
	{
		map.put(FLUIDLOGGABLE_PACKAGE + "." + mixinClassName, Pair.of(config, defaultValue));
	}
	
	private static Map<String, Pair<String, Boolean>> buildMap()
	{
		final Map<String, Pair<String, Boolean>> map = new HashMap<>();
		
		addConfig(map, "AllOfTheBlocksMixin", "absolutely_every_single_existing_block_yes_i_am_sure", false);
		addConfig(map, "BarrierBlockMixin", "barrier", false);
		addConfig(map, "BeaconBlockMixin", "beacon", false);
		addConfig(map, "CauldronBlockMixin", "cauldron", false);
		addConfig(map, "EndPortalBlockMixin", "end_portal", false);
		addConfig(map, "FarmlandBlockMixin", "farmland", false);
		addConfig(map, "FireBlockMixin", "fire", false);
		addConfig(map, "GrassPathBlockMixin", "grass_path", false);
		addConfig(map, "HoneyBlockMixin", "honey_block", false);
		addConfig(map, "LeavesBlockMixin", "leaves", false);
		addConfig(map, "LilyPadBlockMixin", "lily_pad", false);
		addConfig(map, "ObserverBlockMixin", "observer", false);
		addConfig(map, "PistonBlockMixin", "pistons", true);
		addConfig(map, "PortalBlockMixin", "nether_portal", false);
		addConfig(map, "RedstoneBlockMixin", "redstone_block", false);
		addConfig(map, "RedstoneWireBlockMixin", "redstone_dust", true);
		addConfig(map, "ShulkerBoxBlockMixin", "shulker_boxes", false);
		addConfig(map, "SlimeBlockMixin", "slime_block", false);
		addConfig(map, "SpawnerBlockMixin", "spawner", false);
		addConfig(map, "StructureVoidBlockMixin", "structure_void", false);
		addConfig(map, "TorchBlockMixin", "torches", true);
		
		return map;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		if (!mixinClassName.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package for class \"%s\": Expected \"%s\", but found \"%s\".", targetClassName, MIXIN_PACKAGE, mixinClassName)
			);
		}
		else if (mixinClassName.startsWith(FLUIDLOGGABLE_PACKAGE))
		{
			if (!Optional.ofNullable(ToweletteConfig.DATA.get("automaticFluidlogging"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(false))
			{
				return false;
			}
		}
		
		if (
			(mixinClassName.contains(".compat114.") && VersionData.MINOR != 14) ||
			(mixinClassName.contains(".compat114plus.") && VersionData.MINOR < 14) ||
			(mixinClassName.contains(".compat114minus.") && VersionData.MINOR > 14) ||
			(mixinClassName.contains(".compat115.") && VersionData.MINOR != 15) ||
			(mixinClassName.contains(".compat115plus.") && VersionData.MINOR < 15) ||
			(mixinClassName.contains(".compat115minus.") && VersionData.MINOR > 15) ||
			(mixinClassName.contains(".compat116.") && VersionData.MINOR != 16) ||
			(mixinClassName.contains(".compat116plus.") && VersionData.MINOR < 16) ||
			(mixinClassName.contains(".compat116minus.") && VersionData.MINOR > 16) ||
			(mixinClassName.contains(".compat117.") && VersionData.MINOR != 17) ||
			(mixinClassName.contains(".compat117plus.") && VersionData.MINOR < 17) ||
			(mixinClassName.contains(".compat117minus.") && VersionData.MINOR > 17)
		)
		{
			return false;
		}
		
		return Optional.ofNullable(CLASS_CONFIG_MAP.get(mixinClassName)).map(ToweletteMixinConfigPlugin::isConfigEnabled).orElse(true);
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{
		
	}
	
	@Override
	public List<String> getMixins()
	{
		return null;
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
		
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
		
	}
}
