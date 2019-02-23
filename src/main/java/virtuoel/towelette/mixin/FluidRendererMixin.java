package virtuoel.towelette.mixin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.ExtendedBlockView;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin
{
	private static final Map<Identifier, Triple<Sprite, Sprite, Sprite>> FLUID_SPRITE_MAP = new HashMap<>();
	
	@Inject(method = "onResourceReload()V", at = @At("HEAD"))
	protected void onOnResourceReload(CallbackInfo info)
	{
		FLUID_SPRITE_MAP.clear();
		for(Fluid f : Registry.FLUID)
		{
			FLUID_SPRITE_MAP.put(Registry.FLUID.getId(f), Triple.of(FluidUtils.Client.getSpriteForFluid(f, true), FluidUtils.Client.getSpriteForFluid(f, false), FluidUtils.Client.getOverlaySpriteForFluid(f)));
		}
	}
	
	@Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
	public void onTesselate(ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1, BufferBuilder bufferBuilder_1, FluidState fluidState_1, CallbackInfoReturnable<Boolean> info)
	{
		Triple<Sprite, Sprite, Sprite> spriteData = FLUID_SPRITE_MAP.get(Registry.FLUID.getId(fluidState_1.getFluid()));
		Sprite stillSprite = spriteData.getLeft(); // sprites_1[0]
		Sprite flowSprite = spriteData.getMiddle(); // sprites_1[1]
		Sprite overlaySprite = spriteData.getRight(); // waterOverlaySprite
		int int_1 = MinecraftClient.getInstance().getBlockColorMap().getRenderColor(fluidState_1.getBlockState(), extendedBlockView_1, blockPos_1, 0);
		
		// mostly the same from here on
		float float_1 = (float) (int_1 >> 16 & 255) / 255.0F;
		float float_2 = (float) (int_1 >> 8 & 255) / 255.0F;
		float float_3 = (float) (int_1 & 255) / 255.0F;
		boolean boolean_2 = !method_3348(extendedBlockView_1, blockPos_1, Direction.UP, fluidState_1);
		boolean boolean_3 = !method_3348(extendedBlockView_1, blockPos_1, Direction.DOWN, fluidState_1) && !method_3344(extendedBlockView_1, blockPos_1, Direction.DOWN, 0.8888889F);
		boolean boolean_4 = !method_3348(extendedBlockView_1, blockPos_1, Direction.NORTH, fluidState_1);
		boolean boolean_5 = !method_3348(extendedBlockView_1, blockPos_1, Direction.SOUTH, fluidState_1);
		boolean boolean_6 = !method_3348(extendedBlockView_1, blockPos_1, Direction.WEST, fluidState_1);
		boolean boolean_7 = !method_3348(extendedBlockView_1, blockPos_1, Direction.EAST, fluidState_1);
		if(!boolean_2 && !boolean_3 && !boolean_7 && !boolean_6 && !boolean_4 && !boolean_5)
		{
			info.setReturnValue(false);
			return;
		}
		else
		{
			boolean boolean_8 = false;
			float float_8 = method_3346(extendedBlockView_1, blockPos_1, fluidState_1.getFluid());
			float float_9 = method_3346(extendedBlockView_1, blockPos_1.south(), fluidState_1.getFluid());
			float float_10 = method_3346(extendedBlockView_1, blockPos_1.east().south(), fluidState_1.getFluid());
			float float_11 = method_3346(extendedBlockView_1, blockPos_1.east(), fluidState_1.getFluid());
			double double_1 = (double) blockPos_1.getX();
			double double_2 = (double) blockPos_1.getY();
			double double_3 = (double) blockPos_1.getZ();
			float float_25;
			float float_54;
			float float_55;
			float float_31;
			float float_32;
			float float_58;
			float float_59;
			float float_60;
			float float_33;
			if(boolean_2 && !method_3344(extendedBlockView_1, blockPos_1, Direction.UP, Math.min(Math.min(float_8, float_9), Math.min(float_10, float_11))))
			{
				boolean_8 = true;
				float_8 -= 0.001F;
				float_9 -= 0.001F;
				float_10 -= 0.001F;
				float_11 -= 0.001F;
				Vec3d vec3d_1 = fluidState_1.method_15758(extendedBlockView_1, blockPos_1);
				float float_26;
				float float_28;
				float float_30;
				Sprite sprite_2;
				float float_34;
				float float_35;
				float float_36;
				float float_37;
				if(vec3d_1.x == 0.0D && vec3d_1.z == 0.0D)
				{
					sprite_2 = stillSprite;
					float_25 = sprite_2.getU(0.0D);
					float_26 = sprite_2.getV(0.0D);
					float_54 = float_25;
					float_28 = sprite_2.getV(16.0D);
					float_55 = sprite_2.getU(16.0D);
					float_30 = float_28;
					float_31 = float_55;
					float_32 = float_26;
				}
				else
				{
					sprite_2 = flowSprite;
					float_34 = (float) MathHelper.atan2(vec3d_1.z, vec3d_1.x) - 1.5707964F;
					float_35 = MathHelper.sin(float_34) * 0.25F;
					float_36 = MathHelper.cos(float_34) * 0.25F;
					float_37 = 8.0F;
					float_25 = sprite_2.getU((double) (8.0F + (-float_36 - float_35) * 16.0F));
					float_26 = sprite_2.getV((double) (8.0F + (-float_36 + float_35) * 16.0F));
					float_54 = sprite_2.getU((double) (8.0F + (-float_36 + float_35) * 16.0F));
					float_28 = sprite_2.getV((double) (8.0F + (float_36 + float_35) * 16.0F));
					float_55 = sprite_2.getU((double) (8.0F + (float_36 + float_35) * 16.0F));
					float_30 = sprite_2.getV((double) (8.0F + (float_36 - float_35) * 16.0F));
					float_31 = sprite_2.getU((double) (8.0F + (float_36 - float_35) * 16.0F));
					float_32 = sprite_2.getV((double) (8.0F + (-float_36 - float_35) * 16.0F));
				}
				
				float_33 = (float_25 + float_54 + float_55 + float_31) / 4.0F;
				float_34 = (float_26 + float_28 + float_30 + float_32) / 4.0F;
				float_35 = (float) stillSprite.getWidth() / (stillSprite.getMaxU() - stillSprite.getMinU());
				float_36 = (float) stillSprite.getHeight() / (stillSprite.getMaxV() - stillSprite.getMinV());
				float_37 = 4.0F / Math.max(float_36, float_35);
				float_25 = MathHelper.lerp(float_37, float_25, float_33);
				float_54 = MathHelper.lerp(float_37, float_54, float_33);
				float_55 = MathHelper.lerp(float_37, float_55, float_33);
				float_31 = MathHelper.lerp(float_37, float_31, float_33);
				float_26 = MathHelper.lerp(float_37, float_26, float_34);
				float_28 = MathHelper.lerp(float_37, float_28, float_34);
				float_30 = MathHelper.lerp(float_37, float_30, float_34);
				float_32 = MathHelper.lerp(float_37, float_32, float_34);
				int int_2 = method_3343(extendedBlockView_1, blockPos_1);
				int int_3 = int_2 >> 16 & '\uffff';
				int int_4 = int_2 & '\uffff';
				float_58 = 1.0F * float_1;
				float_59 = 1.0F * float_2;
				float_60 = 1.0F * float_3;
				bufferBuilder_1.vertex(double_1 + 0.0D, double_2 + (double) float_8, double_3 + 0.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_25, (double) float_26).texture(int_3, int_4).next();
				bufferBuilder_1.vertex(double_1 + 0.0D, double_2 + (double) float_9, double_3 + 1.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_54, (double) float_28).texture(int_3, int_4).next();
				bufferBuilder_1.vertex(double_1 + 1.0D, double_2 + (double) float_10, double_3 + 1.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_55, (double) float_30).texture(int_3, int_4).next();
				bufferBuilder_1.vertex(double_1 + 1.0D, double_2 + (double) float_11, double_3 + 0.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_31, (double) float_32).texture(int_3, int_4).next();
				if(fluidState_1.method_15756(extendedBlockView_1, blockPos_1.up()))
				{
					bufferBuilder_1.vertex(double_1 + 0.0D, double_2 + (double) float_8, double_3 + 0.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_25, (double) float_26).texture(int_3, int_4).next();
					bufferBuilder_1.vertex(double_1 + 1.0D, double_2 + (double) float_11, double_3 + 0.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_31, (double) float_32).texture(int_3, int_4).next();
					bufferBuilder_1.vertex(double_1 + 1.0D, double_2 + (double) float_10, double_3 + 1.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_55, (double) float_30).texture(int_3, int_4).next();
					bufferBuilder_1.vertex(double_1 + 0.0D, double_2 + (double) float_9, double_3 + 1.0D).color(float_58, float_59, float_60, 1.0F).texture((double) float_54, (double) float_28).texture(int_3, int_4).next();
				}
			}
			
			if(boolean_3)
			{
				float_25 = stillSprite.getMinU();
				float_54 = stillSprite.getMaxU();
				float_55 = stillSprite.getMinV();
				float_31 = stillSprite.getMaxV();
				int int_5 = method_3343(extendedBlockView_1, blockPos_1.down());
				int int_6 = int_5 >> 16 & '\uffff';
				int int_7 = int_5 & '\uffff';
				float_32 = 0.5F * float_1;
				float float_46 = 0.5F * float_2;
				float_33 = 0.5F * float_3;
				bufferBuilder_1.vertex(double_1, double_2, double_3 + 1.0D).color(float_32, float_46, float_33, 1.0F).texture((double) float_25, (double) float_31).texture(int_6, int_7).next();
				bufferBuilder_1.vertex(double_1, double_2, double_3).color(float_32, float_46, float_33, 1.0F).texture((double) float_25, (double) float_55).texture(int_6, int_7).next();
				bufferBuilder_1.vertex(double_1 + 1.0D, double_2, double_3).color(float_32, float_46, float_33, 1.0F).texture((double) float_54, (double) float_55).texture(int_6, int_7).next();
				bufferBuilder_1.vertex(double_1 + 1.0D, double_2, double_3 + 1.0D).color(float_32, float_46, float_33, 1.0F).texture((double) float_54, (double) float_31).texture(int_6, int_7).next();
				boolean_8 = true;
			}
			
			for(int int_8 = 0; int_8 < 4; ++int_8)
			{
				double double_16;
				double double_18;
				double double_17;
				double double_19;
				Direction direction_3;
				boolean boolean_12;
				if(int_8 == 0)
				{
					float_54 = float_8;
					float_55 = float_11;
					double_16 = double_1;
					double_17 = double_1 + 1.0D;
					double_18 = double_3 + 0.001D;
					double_19 = double_3 + 0.001D;
					direction_3 = Direction.NORTH;
					boolean_12 = boolean_4;
				}
				else if(int_8 == 1)
				{
					float_54 = float_10;
					float_55 = float_9;
					double_16 = double_1 + 1.0D;
					double_17 = double_1;
					double_18 = double_3 + 1.0D - 0.001D;
					double_19 = double_3 + 1.0D - 0.001D;
					direction_3 = Direction.SOUTH;
					boolean_12 = boolean_5;
				}
				else if(int_8 == 2)
				{
					float_54 = float_9;
					float_55 = float_8;
					double_16 = double_1 + 0.001D;
					double_17 = double_1 + 0.001D;
					double_18 = double_3 + 1.0D;
					double_19 = double_3;
					direction_3 = Direction.WEST;
					boolean_12 = boolean_6;
				}
				else
				{
					float_54 = float_11;
					float_55 = float_10;
					double_16 = double_1 + 1.0D - 0.001D;
					double_17 = double_1 + 1.0D - 0.001D;
					double_18 = double_3;
					double_19 = double_3 + 1.0D;
					direction_3 = Direction.EAST;
					boolean_12 = boolean_7;
				}
				
				if(boolean_12 && !method_3344(extendedBlockView_1, blockPos_1, direction_3, Math.max(float_54, float_55)))
				{
					boolean_8 = true;
					BlockPos blockPos_2 = blockPos_1.offset(direction_3);
					Sprite sprite_3 = flowSprite;
					if(overlaySprite != MissingSprite.getMissingSprite())
					{
						Block block_1 = extendedBlockView_1.getBlockState(blockPos_2).getBlock();
						if(block_1 instanceof GlassBlock || block_1 instanceof StainedGlassBlock)
						{
							sprite_3 = overlaySprite;
						}
					}
					
					float float_56 = sprite_3.getU(0.0D);
					float float_57 = sprite_3.getU(8.0D);
					float_58 = sprite_3.getV((double) ((1.0F - float_54) * 16.0F * 0.5F));
					float_59 = sprite_3.getV((double) ((1.0F - float_55) * 16.0F * 0.5F));
					float_60 = sprite_3.getV(8.0D);
					int int_9 = method_3343(extendedBlockView_1, blockPos_2);
					int int_10 = int_9 >> 16 & '\uffff';
					int int_11 = int_9 & '\uffff';
					float float_61 = int_8 < 2 ? 0.8F : 0.6F;
					float float_62 = 1.0F * float_61 * float_1;
					float float_63 = 1.0F * float_61 * float_2;
					float float_64 = 1.0F * float_61 * float_3;
					bufferBuilder_1.vertex(double_16, double_2 + (double) float_54, double_18).color(float_62, float_63, float_64, 1.0F).texture((double) float_56, (double) float_58).texture(int_10, int_11).next();
					bufferBuilder_1.vertex(double_17, double_2 + (double) float_55, double_19).color(float_62, float_63, float_64, 1.0F).texture((double) float_57, (double) float_59).texture(int_10, int_11).next();
					bufferBuilder_1.vertex(double_17, double_2 + 0.0D, double_19).color(float_62, float_63, float_64, 1.0F).texture((double) float_57, (double) float_60).texture(int_10, int_11).next();
					bufferBuilder_1.vertex(double_16, double_2 + 0.0D, double_18).color(float_62, float_63, float_64, 1.0F).texture((double) float_56, (double) float_60).texture(int_10, int_11).next();
					if(sprite_3 != overlaySprite)
					{
						bufferBuilder_1.vertex(double_16, double_2 + 0.0D, double_18).color(float_62, float_63, float_64, 1.0F).texture((double) float_56, (double) float_60).texture(int_10, int_11).next();
						bufferBuilder_1.vertex(double_17, double_2 + 0.0D, double_19).color(float_62, float_63, float_64, 1.0F).texture((double) float_57, (double) float_60).texture(int_10, int_11).next();
						bufferBuilder_1.vertex(double_17, double_2 + (double) float_55, double_19).color(float_62, float_63, float_64, 1.0F).texture((double) float_57, (double) float_59).texture(int_10, int_11).next();
						bufferBuilder_1.vertex(double_16, double_2 + (double) float_54, double_18).color(float_62, float_63, float_64, 1.0F).texture((double) float_56, (double) float_58).texture(int_10, int_11).next();
					}
				}
			}
			
			info.setReturnValue(boolean_8);
			return;
		}
	}
	
	// fluid matching
	@Shadow
	private static boolean method_3348(BlockView blockView_1, BlockPos blockPos_1, Direction direction_1, FluidState fluidState_1)
	{
		return false;
	}
	
	// culling
	@Shadow
	private static boolean method_3344(BlockView blockView_1, BlockPos blockPos_1, Direction direction_1, float float_1)
	{
		return false;
	}
	
	// combined light
	@Shadow
	abstract int method_3343(ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1);
	
	// fluid height
	@Shadow
	abstract float method_3346(BlockView blockView_1, BlockPos blockPos_1, Fluid fluid_1);
}
