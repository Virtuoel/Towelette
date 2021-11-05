package virtuoel.towelette.util;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;

public interface ToweletteWorldAccessExtensions
{
	default void towelette_scheduleFluidTick(BlockPos pos, Fluid fluid, int rate)
	{
		
	}
}
