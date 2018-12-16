package virtuoel.towelette.mixin;

import java.io.IOException;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Sets;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.Towelette;
import virtuoel.towelette.util.FluidUtils;

@Mixin(SpriteAtlasTexture.class)
public abstract class SpriteAtlasTextureMixin
{
	@Shadow
	private final Set<Identifier> spritesToLoad = Sets.newHashSet();
	
	@Inject(method = "reload", at = @At("HEAD"))
	protected void onReload(ResourceManager var1, CallbackInfo ci)
	{
		for(Fluid f : Registry.FLUID)
		{
			addSprite(var1, spritesToLoad, FluidUtils.getSpriteIdForFluid(f, true));
			addSprite(var1, spritesToLoad, FluidUtils.getSpriteIdForFluid(f, false));
			addSprite(var1, spritesToLoad, FluidUtils.getOverlaySpriteIdForFluid(f));
		}
	}
	
	private static void addSprite(ResourceManager manager, Set<Identifier> spritesToLoad, Identifier id)
	{
		Identifier path = new Identifier(id.getNamespace(), "textures/" + id.getPath() + ".png");
		try(Resource res = manager.getResource(path))
		{
			spritesToLoad.add(id);
		}
		catch(IOException e)
		{
			Towelette.LOGGER.debug("Could not load fluid sprite {} from {}", id, path);
		}
	}
}
