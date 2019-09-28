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
	default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return FluidUtils.canFillWithFluid(world, pos, state, fluid);
	}
	
	@Override
	default boolean tryFillWithFluid(IWorld world, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		return FluidUtils.tryFillWithFluid(world, pos, blockState, fluidState);
	}
	
	@Override
	default Fluid tryDrainFluid(IWorld world, BlockPos pos, BlockState state)
	{
		return FluidUtils.tryDrainFluid(world, pos, state);
	}
}
