package virtuoel.towelette.hooks;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;

public class FluidloggableHooks
{
	public static void hookScheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		final Fluid fluid = FluidProperty.FLUID.getFluid(state);
		if(fluid != Fluids.EMPTY)
		{
			world.getFluidTickScheduler().schedule(pos, fluid, fluid.getTickRate(world));
		}
	}
}
