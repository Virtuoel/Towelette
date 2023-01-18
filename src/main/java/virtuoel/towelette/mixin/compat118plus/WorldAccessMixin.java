package virtuoel.towelette.mixin.compat118plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.ToweletteWorldAccessExtensions;

@Mixin(WorldAccess.class)
public interface WorldAccessMixin extends ToweletteWorldAccessExtensions
{
	@Override
	default void towelette_scheduleFluidTick(BlockPos pos, Fluid fluid, int rate)
	{
		((WorldAccess) this).scheduleFluidTick(pos, fluid, rate);
	}
}
