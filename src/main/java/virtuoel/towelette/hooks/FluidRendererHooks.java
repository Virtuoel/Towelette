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

public class FluidRendererHooks
{
	public static final Map<Identifier, Triple<Sprite, Sprite, Sprite>> FLUID_SPRITE_MAP = new HashMap<>();
	
	// private static methods from FluidRenderer
	// TODO make proper names
	
	public static boolean doesFluidMatch(BlockView blockView_1, BlockPos blockPos_1, Direction direction_1, FluidState fluidState_1)
	{
		BlockPos offset = blockPos_1.offset(direction_1);
		FluidState other = blockView_1.getFluidState(offset);
		return other.getFluid().matchesType(fluidState_1.getFluid());
	}
	
	public static boolean voxelCullingMaybe(BlockView blockView_1, BlockPos blockPos_1, Direction direction_1, float height)
	{
		BlockPos blockPos_2 = blockPos_1.offset(direction_1);
		BlockState blockState_1 = blockView_1.getBlockState(blockPos_2);
		if(blockState_1.isFullBoundsCubeForCulling())
		{
			VoxelShape voxelShape_1 = VoxelShapes.cube(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D);
			VoxelShape voxelShape_2 = blockState_1.method_11615(blockView_1, blockPos_2);
			return VoxelShapes.method_1083(voxelShape_1, voxelShape_2, direction_1);
		}
		else
		{
			return false;
		}
	}
	
	public static int packedLightMaybe(ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1)
	{
		int int_1 = extendedBlockView_1.getLightmapIndex(blockPos_1, 0);
		int int_2 = extendedBlockView_1.getLightmapIndex(blockPos_1.up(), 0);
		int int_3 = int_1 & 255;
		int int_4 = int_2 & 255;
		int int_5 = int_1 >> 16 & 255;
		int int_6 = int_2 >> 16 & 255;
		return (int_3 > int_4 ? int_3 : int_4) | (int_5 > int_6 ? int_5 : int_6) << 16;
	}
	
	public static float fluidHeightMaybe(BlockView blockView_1, BlockPos blockPos_1, Fluid fluid_1)
	{
		int int_1 = 0;
		float float_1 = 0.0F;
		
		for(int int_2 = 0; int_2 < 4; ++int_2)
		{
			BlockPos blockPos_2 = blockPos_1.add(-(int_2 & 1), 0, -(int_2 >> 1 & 1));
			if(blockView_1.getFluidState(blockPos_2.up()).getFluid().matchesType(fluid_1))
			{
				return 1.0F;
			}
			
			FluidState fluidState_1 = blockView_1.getFluidState(blockPos_2);
			if(fluidState_1.getFluid().matchesType(fluid_1))
			{
				float float_2 = fluidState_1.getHeight(blockView_1, blockPos_2);
				if(float_2 >= 0.8F)
				{
					float_1 += float_2 * 10.0F;
					int_1 += 10;
				}
				else
				{
					float_1 += float_2;
					++int_1;
				}
			}
			else if(!blockView_1.getBlockState(blockPos_2).getMaterial().method_15799())
			{
				++int_1;
			}
		}
		
		return float_1 / (float) int_1;
	}
}
