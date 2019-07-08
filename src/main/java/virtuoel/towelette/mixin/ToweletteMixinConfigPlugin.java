package virtuoel.towelette.mixin;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import virtuoel.towelette.api.ToweletteConfig;

public class ToweletteMixinConfigPlugin implements IMixinConfigPlugin
{
	private static final String MIXIN_PACKAGE = "virtuoel.towelette.mixin";
	
	@Override
	public void onLoad(String mixinPackage)
	{
		if(!mixinPackage.startsWith(MIXIN_PACKAGE))
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
	
	private static final String FLUIDLOGGABLE_PACKAGE = MIXIN_PACKAGE + ".fluidloggable";
	
	private static boolean isConfigEnabled(String name, boolean defaultValue)
	{
		return Optional.ofNullable(ToweletteConfig.DATA.get("fluidlogging"))
			.filter(JsonElement::isJsonObject)
			.map(JsonElement::getAsJsonObject)
			.map(o -> o.get(name))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean)
			.map(JsonElement::getAsBoolean)
			.orElse(defaultValue);
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		if(!mixinClassName.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package for class \"%s\": Expected \"%s\", but found \"%s\".", targetClassName, MIXIN_PACKAGE, mixinClassName)
			);
		}
		
		try
		{
			switch(mixinClassName)
			{
				case FLUIDLOGGABLE_PACKAGE + ".BarrierBlockMixin":
					return isConfigEnabled("barrier", false);
				case FLUIDLOGGABLE_PACKAGE + ".BeaconBlockMixin":
					return isConfigEnabled("beacon", false);
				case FLUIDLOGGABLE_PACKAGE + ".CauldronBlockMixin":
					return isConfigEnabled("cauldron", false);
				case FLUIDLOGGABLE_PACKAGE + ".EndPortalBlockMixin":
					return isConfigEnabled("end_portal", false);
				case FLUIDLOGGABLE_PACKAGE + ".FarmlandBlockMixin":
					return isConfigEnabled("farmland", false);
				case FLUIDLOGGABLE_PACKAGE + ".FireBlockMixin":
					return isConfigEnabled("fire", false);
				case FLUIDLOGGABLE_PACKAGE + ".GrassPathBlockMixin":
					return isConfigEnabled("grass_path", false);
				case FLUIDLOGGABLE_PACKAGE + ".LeavesBlockMixin":
					return isConfigEnabled("leaves", false);
				case FLUIDLOGGABLE_PACKAGE + ".LilyPadBlockMixin":
					return isConfigEnabled("lily_pad", false);
				case FLUIDLOGGABLE_PACKAGE + ".PistonBlockMixin":
					return isConfigEnabled("pistons", false);
				case FLUIDLOGGABLE_PACKAGE + ".PortalBlockMixin":
					return isConfigEnabled("nether_portal", false);
				case FLUIDLOGGABLE_PACKAGE + ".RedstoneWireBlockMixin":
					return isConfigEnabled("redstone_dust", true);
				case FLUIDLOGGABLE_PACKAGE + ".ShulkerBoxBlockMixin":
					return isConfigEnabled("shulker_boxes", false);
				case FLUIDLOGGABLE_PACKAGE + ".SpawnerBlockMixin":
					return isConfigEnabled("spawner", false);
				case FLUIDLOGGABLE_PACKAGE + ".StructureVoidBlockMixin":
					return isConfigEnabled("structure_void", false);
				case FLUIDLOGGABLE_PACKAGE + ".TorchBlockMixin":
					return isConfigEnabled("torches", true);
				default:
					return true;
			}
		}
		catch(UnsupportedOperationException | ClassCastException e)
		{
			e.printStackTrace();
			return false;
		}
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
