package virtuoel.towelette.mixin.compat116minus.towelette;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Fluidloggable.class)
public interface FluidloggableMixin
{
	default Fluid method_9700(WorldAccess world, BlockPos pos, BlockState state)
	{
		return FluidUtils.tryDrainFluid(world, pos, state);
	}
}
