package virtuoel.towelette.mixin.compat117minus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteTickSchedulerExtensions;
import virtuoel.towelette.util.ToweletteWorldAccessExtensions;

@Mixin(WorldAccess.class)
public interface WorldAccessMixin extends ToweletteWorldAccessExtensions
{
	@SuppressWarnings("unchecked")
	@Override
	default void towelette_scheduleFluidTick(BlockPos pos, Fluid fluid, int rate)
	{
		final Object scheduler = FluidUtils.getFluidTickScheduler((WorldAccess) this);
		if (scheduler != null)
		{
			((ToweletteTickSchedulerExtensions<Fluid>) scheduler).towelette_schedule(pos, fluid, rate);
		}
	}
}
