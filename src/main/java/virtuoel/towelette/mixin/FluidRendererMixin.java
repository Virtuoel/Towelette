package virtuoel.towelette.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ExtendedBlockView;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin
{
	private final Map<Identifier, Pair<Sprite[], Sprite>> FLUID_SPRITE_MAP = new HashMap<>();
	
	@Inject(method = "onResourceReload()V", at = @At("HEAD"))
	protected void onOnResourceReload(CallbackInfo info)
	{
		FLUID_SPRITE_MAP.clear();
		for(Fluid f : Registry.FLUID)
		{
			FLUID_SPRITE_MAP.put(Registry.FLUID.getId(f), new Pair<Sprite[], Sprite>(new Sprite[] { FluidUtils.Client.getSpriteForFluid(f, true), FluidUtils.Client.getSpriteForFluid(f, false) }, FluidUtils.Client.getOverlaySpriteForFluid(f)));
		}
	}
	
	private final ThreadLocal<Pair<Sprite[], Sprite>> cachedSpriteData = ThreadLocal.withInitial(() -> new Pair<Sprite[], Sprite>(new Sprite[] { FluidUtils.Client.MISSING_SPRITE, FluidUtils.Client.MISSING_SPRITE }, FluidUtils.Client.MISSING_SPRITE));
	
	@Inject(method = "tesselate", at = @At("HEAD"))
	public void onPreTesselate(ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1, BufferBuilder bufferBuilder_1, FluidState fluidState_1, CallbackInfoReturnable<Boolean> info)
	{
		cachedSpriteData.set(FLUID_SPRITE_MAP.get(Registry.FLUID.getId(fluidState_1.getFluid())));
	}
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;matches(Lnet/minecraft/tag/Tag;)Z"))
	public boolean onTesselateMatchesProxy(FluidState obj)
	{
		return false;
	}
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BiomeColors;waterColorAt(Lnet/minecraft/world/ExtendedBlockView;Lnet/minecraft/util/math/BlockPos;)I"))
	public int onTesselateWaterColorAtProxy(ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1)
	{
		return MinecraftClient.getInstance().getBlockColorMap().getRenderColor(extendedBlockView_1.getFluidState(blockPos_1).getBlockState(), extendedBlockView_1, blockPos_1, 0);
	}
	
	@Redirect(method = "tesselate", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/block/FluidRenderer;waterSprites:[Lnet/minecraft/client/texture/Sprite;"))
	public Sprite[] onTesselateWaterSpritesProxy(FluidRenderer obj)
	{
		return cachedSpriteData.get().getLeft();
	}
	
	private static final double BOTTOM_OFFSET = 0.001D;
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", ordinal = 8, target = "Lnet/minecraft/client/render/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/BufferBuilder;"))
	public BufferBuilder onTesselateVertex1Proxy(BufferBuilder obj, double double_1, double double_2, double double_3)
	{
		return obj.vertex(double_1, double_2 + BOTTOM_OFFSET, double_3);
	}
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", ordinal = 9, target = "Lnet/minecraft/client/render/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/BufferBuilder;"))
	public BufferBuilder onTesselateVertex2Proxy(BufferBuilder obj, double double_1, double double_2, double double_3)
	{
		return obj.vertex(double_1, double_2 + BOTTOM_OFFSET, double_3);
	}
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", ordinal = 10, target = "Lnet/minecraft/client/render/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/BufferBuilder;"))
	public BufferBuilder onTesselateVertex3Proxy(BufferBuilder obj, double double_1, double double_2, double double_3)
	{
		return obj.vertex(double_1, double_2 + BOTTOM_OFFSET, double_3);
	}
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", ordinal = 11, target = "Lnet/minecraft/client/render/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/BufferBuilder;"))
	public BufferBuilder onTesselateVertex4Proxy(BufferBuilder obj, double double_1, double double_2, double double_3)
	{
		return obj.vertex(double_1, double_2 + BOTTOM_OFFSET, double_3);
	}
	
	@Redirect(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ExtendedBlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public BlockState onTesselateGetBlockStateProxy(ExtendedBlockView obj, BlockPos pos)
	{
		if(cachedSpriteData.get().getRight() != FluidUtils.Client.MISSING_SPRITE)
		{
			final BlockState b = obj.getBlockState(pos);
			return b.getBlock() instanceof GlassBlock ? Blocks.GLASS.getDefaultState() : b;
		}
		return Blocks.AIR.getDefaultState();
	}
	
	@Redirect(method = "tesselate", at = @At(value = "FIELD", ordinal = 0, target = "Lnet/minecraft/client/render/block/FluidRenderer;waterOverlaySprite:Lnet/minecraft/client/texture/Sprite;"))
	public Sprite onTesselateWaterOverlaySprite1Proxy(FluidRenderer obj)
	{
		return cachedSpriteData.get().getRight();
	}
	
	@Redirect(method = "tesselate", at = @At(value = "FIELD", ordinal = 1, target = "Lnet/minecraft/client/render/block/FluidRenderer;waterOverlaySprite:Lnet/minecraft/client/texture/Sprite;"))
	public Sprite onTesselateWaterOverlaySprite2Proxy(FluidRenderer obj)
	{
		return FluidUtils.Client.MISSING_SPRITE;
	}
	
	@Inject(method = "tesselate", at = @At("RETURN"))
	public void onPostTesselate(ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1, BufferBuilder bufferBuilder_1, FluidState fluidState_1, CallbackInfoReturnable<Boolean> info)
	{
		cachedSpriteData.remove();
	}
}
