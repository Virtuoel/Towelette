package virtuoel.towelette.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidUtils
{
	public static Identifier getIdForStillFluid(Fluid fluid)
	{
		if(!fluid.getDefaultState().isStill() && fluid instanceof BaseFluid)
		{
			fluid = ((BaseFluid) fluid).getStill();
		}
		return Registry.FLUID.getId(fluid);
	}
	
	public static Identifier getSpriteIdForStillFluid(Fluid fluid)
	{
		Identifier id = getIdForStillFluid(fluid);
		return new Identifier(id.getNamespace(), "block/" + id.getPath());
	}
	
	public static Identifier getSpriteIdForFluid(Fluid fluid, String suffix)
	{
		return new Identifier(getSpriteIdForStillFluid(fluid).toString() + suffix);
	}
	
	public static Identifier getSpriteIdForFluid(Fluid fluid, boolean still)
	{
		return getSpriteIdForFluid(fluid, still ? "_still" : "_flow");
	}
	
	public static Sprite getSpriteForFluid(Fluid fluid, boolean still)
	{
		return getSprite(getSpriteIdForFluid(fluid, still));
	}
	
	public static Identifier getOverlaySpriteIdForFluid(Fluid fluid)
	{
		return getSpriteIdForFluid(fluid, "_overlay");
	}
	
	public static Sprite getOverlaySpriteForFluid(Fluid fluid)
	{
		return getSprite(getOverlaySpriteIdForFluid(fluid));
	}
	
	public static Sprite getSprite(Identifier id)
	{
		return MinecraftClient.getInstance().getSpriteAtlas().getSprite(id);
	}
}
