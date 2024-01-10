package virtuoel.towelette.mixin.compat1201minus.towelette;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Fluidloggable.class)
public interface FluidloggableMixin
{
	default boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return method_10310(world, pos, state, fluid);
	}
	
	default boolean method_10310(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return FluidUtils.canFillWithFluid(world, pos, state, fluid);
	}
}
