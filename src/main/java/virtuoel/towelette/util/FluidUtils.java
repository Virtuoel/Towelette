package virtuoel.towelette.util;

import java.io.IOException;
import java.util.Optional;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.Towelette;

public class FluidUtils
{
	public static final Sprite MISSING_SPRITE = MissingSprite.getMissingSprite();
	
	public static Identifier getIdForStillFluid(Fluid fluid)
	{
		if(!fluid.getDefaultState().isStill() && fluid instanceof BaseFluid)
		{
			fluid = ((BaseFluid) fluid).getStill();
		}
		return Registry.FLUID.getId(fluid);
	}
	
	public static Identifier withBlockPath(Identifier id)
	{
		return new Identifier(id.getNamespace(), "block/" + id.getPath());
	}
	
	public static Identifier toTexturePath(Identifier id)
	{
		return new Identifier(id.getNamespace(), "textures/" + id.getPath() + ".png");
	}
	
	public static Optional<Identifier> getSpriteIdForTexture(Identifier id)
	{
		Identifier path = toTexturePath(id);
		try(Resource res = MinecraftClient.getInstance().getResourceManager().getResource(path))
		{
			return Optional.of(id);
		}
		catch(IOException e)
		{
			Towelette.LOGGER.debug("Could not load sprite {} from {}", id, path);
			return Optional.empty();
		}
	}
	
	public static Optional<Identifier> getSpriteIdForFluid(Fluid fluid, String suffix)
	{
		Identifier id = withBlockPath(new Identifier(getIdForStillFluid(fluid).toString() + suffix));
		
		return getSpriteIdForTexture(id);
	}
	
	public static Optional<Identifier> getSpriteIdForFluid(Fluid fluid, boolean still)
	{
		return getSpriteIdForFluid(fluid, still ? "_still" : "_flow");
	}
	
	public static Sprite getSpriteForFluid(Fluid fluid, boolean still)
	{
		return getSprite(getSpriteIdForFluid(fluid, still));
	}
	
	public static Optional<Identifier> getOverlaySpriteIdForFluid(Fluid fluid)
	{
		return getSpriteIdForFluid(fluid, "_overlay");
	}
	
	public static Sprite getOverlaySpriteForFluid(Fluid fluid)
	{
		return getSprite(getOverlaySpriteIdForFluid(fluid));
	}
	
	public static Sprite getSprite(Optional<Identifier> id)
	{
		return id.map(MinecraftClient.getInstance().getSpriteAtlas()::getSprite).orElse(MISSING_SPRITE);
	}
}
