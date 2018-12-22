package virtuoel.towelette.mixin;

import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ExtendedBlockView;
import virtuoel.towelette.hooks.BlockLiquidRendererHooks;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin
{
	@Inject(method = "onResourceReload()V", at = @At("HEAD"))
	protected void onOnResourceReload(CallbackInfo ci)
	{
		BlockLiquidRendererHooks.FLUID_SPRITE_MAP.clear();
		for(Fluid f : Registry.FLUID)
		{
			BlockLiquidRendererHooks.FLUID_SPRITE_MAP.put(Registry.FLUID.getId(f), Triple.of(FluidUtils.getSpriteForFluid(f, true), FluidUtils.getSpriteForFluid(f, false), FluidUtils.getOverlaySpriteForFluid(f)));
		}
	}
	
	@Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
	public void onTesselate(ExtendedBlockView var1, BlockPos var2, BufferBuilder var3, FluidState var4, CallbackInfoReturnable<Boolean> cir)
	{
		Triple<Sprite, Sprite, Sprite> spriteData = BlockLiquidRendererHooks.FLUID_SPRITE_MAP.get(Registry.FLUID.getId(var4.getFluid()));
		Sprite stillSprite = spriteData.getLeft(); // var6[0]
		Sprite flowSprite = spriteData.getMiddle(); // var6[1]
		Sprite overlaySprite = spriteData.getRight(); // waterOverlaySprite
		int var7 = MinecraftClient.getInstance().getBlockColorMap().getRenderColor(var4.getBlockState(), var1, var2, 0);
		
		// mostly the same from here on
		float var8 = (float) (var7 >> 16 & 255) / 255.0F;
		float var9 = (float) (var7 >> 8 & 255) / 255.0F;
		float var10 = (float) (var7 & 255) / 255.0F;
		boolean var11 = !BlockLiquidRendererHooks.doesFluidMatch(var1, var2, Direction.UP, var4);
		boolean var12 = !BlockLiquidRendererHooks.doesFluidMatch(var1, var2, Direction.DOWN, var4) && !BlockLiquidRendererHooks.voxelCullingMaybe(var1, var2, Direction.DOWN, 0.8888889F);
		boolean var13 = !BlockLiquidRendererHooks.doesFluidMatch(var1, var2, Direction.NORTH, var4);
		boolean var14 = !BlockLiquidRendererHooks.doesFluidMatch(var1, var2, Direction.SOUTH, var4);
		boolean var15 = !BlockLiquidRendererHooks.doesFluidMatch(var1, var2, Direction.WEST, var4);
		boolean var16 = !BlockLiquidRendererHooks.doesFluidMatch(var1, var2, Direction.EAST, var4);
		if(!var11 && !var12 && !var16 && !var15 && !var13 && !var14)
		{
			cir.setReturnValue(false);
			return;
		}
		else
		{
			boolean var17 = false;
			float var22 = BlockLiquidRendererHooks.fluidHeightMaybe(var1, var2, var4.getFluid());
			float var23 = BlockLiquidRendererHooks.fluidHeightMaybe(var1, var2.south(), var4.getFluid());
			float var24 = BlockLiquidRendererHooks.fluidHeightMaybe(var1, var2.east().south(), var4.getFluid());
			float var25 = BlockLiquidRendererHooks.fluidHeightMaybe(var1, var2.east(), var4.getFluid());
			double var26 = (double) var2.getX();
			double var28 = (double) var2.getY();
			double var30 = (double) var2.getZ();
			float var33;
			float var34;
			float var35;
			float var36;
			float var40;
			float var50;
			float var51;
			float var52;
			float var68;
			if(var11 && !BlockLiquidRendererHooks.voxelCullingMaybe(var1, var2, Direction.UP, Math.min(Math.min(var22, var23), Math.min(var24, var25))))
			{
				var17 = true;
				var22 -= 0.001F;
				var23 -= 0.001F;
				var24 -= 0.001F;
				var25 -= 0.001F;
				Vec3d var41 = var4.method_15758(var1, var2);
				float var37;
				float var38;
				float var39;
				Sprite var42;
				float var43;
				float var44;
				float var45;
				float var46;
				if(var41.x == 0.0D && var41.z == 0.0D)
				{
					var42 = stillSprite;
					var33 = var42.getU(0.0D);
					var37 = var42.getV(0.0D);
					var34 = var33;
					var38 = var42.getV(16.0D);
					var35 = var42.getU(16.0D);
					var39 = var38;
					var36 = var35;
					var40 = var37;
				}
				else
				{
					var42 = flowSprite;
					var43 = (float) MathHelper.atan2(var41.z, var41.x) - 1.5707964F;
					var44 = MathHelper.sin(var43) * 0.25F;
					var45 = MathHelper.cos(var43) * 0.25F;
					var46 = 8.0F;
					var33 = var42.getU((double) (8.0F + (-var45 - var44) * 16.0F));
					var37 = var42.getV((double) (8.0F + (-var45 + var44) * 16.0F));
					var34 = var42.getU((double) (8.0F + (-var45 + var44) * 16.0F));
					var38 = var42.getV((double) (8.0F + (var45 + var44) * 16.0F));
					var35 = var42.getU((double) (8.0F + (var45 + var44) * 16.0F));
					var39 = var42.getV((double) (8.0F + (var45 - var44) * 16.0F));
					var36 = var42.getU((double) (8.0F + (var45 - var44) * 16.0F));
					var40 = var42.getV((double) (8.0F + (-var45 - var44) * 16.0F));
				}
				
				var68 = (var33 + var34 + var35 + var36) / 4.0F;
				var43 = (var37 + var38 + var39 + var40) / 4.0F;
				var44 = (float) stillSprite.getWidth() / (stillSprite.getMaxU() - stillSprite.getMinU());
				var45 = (float) stillSprite.getHeight() / (stillSprite.getMaxV() - stillSprite.getMinV());
				var46 = 4.0F / Math.max(var45, var44);
				var33 = MathHelper.lerp(var46, var33, var68);
				var34 = MathHelper.lerp(var46, var34, var68);
				var35 = MathHelper.lerp(var46, var35, var68);
				var36 = MathHelper.lerp(var46, var36, var68);
				var37 = MathHelper.lerp(var46, var37, var43);
				var38 = MathHelper.lerp(var46, var38, var43);
				var39 = MathHelper.lerp(var46, var39, var43);
				var40 = MathHelper.lerp(var46, var40, var43);
				int var47 = BlockLiquidRendererHooks.packedLightMaybe(var1, var2);
				int var48 = var47 >> 16 & '\uffff';
				int var49 = var47 & '\uffff';
				var50 = 1.0F * var8;
				var51 = 1.0F * var9;
				var52 = 1.0F * var10;
				var3.vertex(var26 + 0.0D, var28 + (double) var22, var30 + 0.0D).color(var50, var51, var52, 1.0F).texture((double) var33, (double) var37).texture(var48, var49).next();
				var3.vertex(var26 + 0.0D, var28 + (double) var23, var30 + 1.0D).color(var50, var51, var52, 1.0F).texture((double) var34, (double) var38).texture(var48, var49).next();
				var3.vertex(var26 + 1.0D, var28 + (double) var24, var30 + 1.0D).color(var50, var51, var52, 1.0F).texture((double) var35, (double) var39).texture(var48, var49).next();
				var3.vertex(var26 + 1.0D, var28 + (double) var25, var30 + 0.0D).color(var50, var51, var52, 1.0F).texture((double) var36, (double) var40).texture(var48, var49).next();
				if(var4.method_15756(var1, var2.up()))
				{
					var3.vertex(var26 + 0.0D, var28 + (double) var22, var30 + 0.0D).color(var50, var51, var52, 1.0F).texture((double) var33, (double) var37).texture(var48, var49).next();
					var3.vertex(var26 + 1.0D, var28 + (double) var25, var30 + 0.0D).color(var50, var51, var52, 1.0F).texture((double) var36, (double) var40).texture(var48, var49).next();
					var3.vertex(var26 + 1.0D, var28 + (double) var24, var30 + 1.0D).color(var50, var51, var52, 1.0F).texture((double) var35, (double) var39).texture(var48, var49).next();
					var3.vertex(var26 + 0.0D, var28 + (double) var23, var30 + 1.0D).color(var50, var51, var52, 1.0F).texture((double) var34, (double) var38).texture(var48, var49).next();
				}
			}
			
			if(var12)
			{
				var33 = stillSprite.getMinU();
				var34 = stillSprite.getMaxU();
				var35 = stillSprite.getMinV();
				var36 = stillSprite.getMaxV();
				int var62 = BlockLiquidRendererHooks.packedLightMaybe(var1, var2.down());
				int var63 = var62 >> 16 & '\uffff';
				int var65 = var62 & '\uffff';
				var40 = 0.5F * var8;
				float var66 = 0.5F * var9;
				var68 = 0.5F * var10;
				var3.vertex(var26, var28, var30 + 1.0D).color(var40, var66, var68, 1.0F).texture((double) var33, (double) var36).texture(var63, var65).next();
				var3.vertex(var26, var28, var30).color(var40, var66, var68, 1.0F).texture((double) var33, (double) var35).texture(var63, var65).next();
				var3.vertex(var26 + 1.0D, var28, var30).color(var40, var66, var68, 1.0F).texture((double) var34, (double) var35).texture(var63, var65).next();
				var3.vertex(var26 + 1.0D, var28, var30 + 1.0D).color(var40, var66, var68, 1.0F).texture((double) var34, (double) var36).texture(var63, var65).next();
				var17 = true;
			}
			
			for(int var60 = 0; var60 < 4; ++var60)
			{
				double var61;
				double var64;
				double var67;
				double var69;
				Direction var70;
				boolean var71;
				if(var60 == 0)
				{
					var34 = var22;
					var35 = var25;
					var61 = var26;
					var67 = var26 + 1.0D;
					var64 = var30 + 0.001D;
					var69 = var30 + 0.001D;
					var70 = Direction.NORTH;
					var71 = var13;
				}
				else if(var60 == 1)
				{
					var34 = var24;
					var35 = var23;
					var61 = var26 + 1.0D;
					var67 = var26;
					var64 = var30 + 1.0D - 0.001D;
					var69 = var30 + 1.0D - 0.001D;
					var70 = Direction.SOUTH;
					var71 = var14;
				}
				else if(var60 == 2)
				{
					var34 = var23;
					var35 = var22;
					var61 = var26 + 0.001D;
					var67 = var26 + 0.001D;
					var64 = var30 + 1.0D;
					var69 = var30;
					var70 = Direction.WEST;
					var71 = var15;
				}
				else
				{
					var34 = var25;
					var35 = var24;
					var61 = var26 + 1.0D - 0.001D;
					var67 = var26 + 1.0D - 0.001D;
					var64 = var30;
					var69 = var30 + 1.0D;
					var70 = Direction.EAST;
					var71 = var16;
				}
				
				if(var71 && !BlockLiquidRendererHooks.voxelCullingMaybe(var1, var2, var70, Math.max(var34, var35)))
				{
					var17 = true;
					BlockPos var72 = var2.offset(var70);
					Sprite var73 = flowSprite;
					if(overlaySprite != MissingSprite.getMissingSprite())
					{
						Block var74 = var1.getBlockState(var72).getBlock();
						if(var74 instanceof GlassBlock || var74 instanceof StainedGlassBlock)
						{
							var73 = overlaySprite;
						}
					}
					
					float var75 = var73.getU(0.0D);
					float var76 = var73.getU(8.0D);
					var50 = var73.getV((double) ((1.0F - var34) * 16.0F * 0.5F));
					var51 = var73.getV((double) ((1.0F - var35) * 16.0F * 0.5F));
					var52 = var73.getV(8.0D);
					int var53 = BlockLiquidRendererHooks.packedLightMaybe(var1, var72);
					int var54 = var53 >> 16 & '\uffff';
					int var55 = var53 & '\uffff';
					float var56 = var60 < 2 ? 0.8F : 0.6F;
					float var57 = 1.0F * var56 * var8;
					float var58 = 1.0F * var56 * var9;
					float var59 = 1.0F * var56 * var10;
					var3.vertex(var61, var28 + (double) var34, var64).color(var57, var58, var59, 1.0F).texture((double) var75, (double) var50).texture(var54, var55).next();
					var3.vertex(var67, var28 + (double) var35, var69).color(var57, var58, var59, 1.0F).texture((double) var76, (double) var51).texture(var54, var55).next();
					var3.vertex(var67, var28 + 0.0D, var69).color(var57, var58, var59, 1.0F).texture((double) var76, (double) var52).texture(var54, var55).next();
					var3.vertex(var61, var28 + 0.0D, var64).color(var57, var58, var59, 1.0F).texture((double) var75, (double) var52).texture(var54, var55).next();
					if(var73 != overlaySprite)
					{
						var3.vertex(var61, var28 + 0.0D, var64).color(var57, var58, var59, 1.0F).texture((double) var75, (double) var52).texture(var54, var55).next();
						var3.vertex(var67, var28 + 0.0D, var69).color(var57, var58, var59, 1.0F).texture((double) var76, (double) var52).texture(var54, var55).next();
						var3.vertex(var67, var28 + (double) var35, var69).color(var57, var58, var59, 1.0F).texture((double) var76, (double) var51).texture(var54, var55).next();
						var3.vertex(var61, var28 + (double) var34, var64).color(var57, var58, var59, 1.0F).texture((double) var75, (double) var50).texture(var54, var55).next();
					}
				}
			}
			
			cir.setReturnValue(var17);
			return;
		}
	}
}
