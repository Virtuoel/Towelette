package virtuoel.towelette.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;

public interface Fluidloggable extends Waterloggable
{
	@Override
	default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		return FluidUtils.tryFillWithFluid(world, pos, blockState, fluidState);
	}
}
