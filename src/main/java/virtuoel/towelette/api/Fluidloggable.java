package virtuoel.towelette.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public interface Fluidloggable extends Waterloggable
{
	@Override
	default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return !state.contains(Properties.WATERLOGGED) ? false : Waterloggable.super.canFillWithFluid(world, pos, state, fluid);
	}
	
	@Override
	default boolean tryFillWithFluid(IWorld world, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		return !blockState.contains(Properties.WATERLOGGED) ? false : Waterloggable.super.tryFillWithFluid(world, pos, blockState, fluidState);
	}
	
	@Override
	default Fluid tryDrainFluid(IWorld world, BlockPos pos, BlockState state)
	{
		return !state.contains(Properties.WATERLOGGED) ? Fluids.EMPTY : Waterloggable.super.tryDrainFluid(world, pos, state);
	}
}
