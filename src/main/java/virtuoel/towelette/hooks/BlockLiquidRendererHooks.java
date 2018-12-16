package virtuoel.towelette.hooks;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.ExtendedBlockView;

public class BlockLiquidRendererHooks
{
	public static final Map<Identifier, Triple<Sprite, Sprite, Sprite>> FLUID_SPRITE_MAP = new HashMap<>();
	
	// private static methods from BlockLiquidRenderer
	// TODO make proper names
	
	public static boolean doesFluidMatch(BlockView view, BlockPos pos, Direction dir, FluidState fluid)
	{
		BlockPos offset = pos.offset(dir);
		FluidState other = view.getFluidState(offset);
		return other.getFluid().matchesType(fluid.getFluid());
	}
	
	public static boolean voxelCullingMaybe(BlockView view, BlockPos pos, Direction dir, float height)
	{
		BlockPos offset = pos.offset(dir);
		BlockState block = view.getBlockState(offset);
		if(block.isFullBoundsCubeForCulling())
		{
			VoxelShape heightCube = VoxelShapes.cube(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D);
			VoxelShape boundsMaybe = block.method_11615(view, offset);
			return VoxelShapes.method_1083(heightCube, boundsMaybe, dir);
		}
		else
		{
			return false;
		}
	}
	
	public static int packedLightMaybe(ExtendedBlockView var1, BlockPos var2)
	{
		int var3 = var1.getLightmapIndex(var2, 0);
		int var4 = var1.getLightmapIndex(var2.up(), 0);
		int var5 = var3 & 255;
		int var6 = var4 & 255;
		int var7 = var3 >> 16 & 255;
		int var8 = var4 >> 16 & 255;
		return (var5 > var6 ? var5 : var6) | (var7 > var8 ? var7 : var8) << 16;
	}
	
	public static float fluidHeightMaybe(BlockView var1, BlockPos var2, Fluid var3)
	{
		int var4 = 0;
		float var5 = 0.0F;
		
		for(int var6 = 0; var6 < 4; ++var6)
		{
			BlockPos var7 = var2.add(-(var6 & 1), 0, -(var6 >> 1 & 1));
			if(var1.getFluidState(var7.up()).getFluid().matchesType(var3))
			{
				return 1.0F;
			}
			
			FluidState var8 = var1.getFluidState(var7);
			if(var8.getFluid().matchesType(var3))
			{
				if(var8.method_15763() >= 0.8F)
				{
					var5 += var8.method_15763() * 10.0F;
					var4 += 10;
				}
				else
				{
					var5 += var8.method_15763();
					++var4;
				}
			}
			else if(!var1.getBlockState(var7).getMaterial().method_15799())
			{
				++var4;
			}
		}
		
		return var5 / (float) var4;
	}
}
