package virtuoel.towelette.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

public interface Fluidloggable extends Waterloggable
{
	@Override
	default boolean canFillWithFluid(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1, Fluid fluid_1)
	{
		return FluidUtils.canFillWithFluid(blockView_1, blockPos_1, blockState_1, fluid_1);
	}
	
	@Override
	default boolean tryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, FluidState fluidState_1)
	{
		return FluidUtils.tryFillWithFluid(iWorld_1, blockPos_1, blockState_1, fluidState_1);
	}
	
	@Override
	default Fluid tryDrainFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1)
	{
		return FluidUtils.tryDrainFluid(iWorld_1, blockPos_1, blockState_1);
	}
}
