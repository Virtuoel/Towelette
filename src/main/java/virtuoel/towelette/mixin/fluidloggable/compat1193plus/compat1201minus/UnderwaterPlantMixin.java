package virtuoel.towelette.mixin.fluidloggable.compat1193plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.KelpBlock;
import net.minecraft.block.KelpPlantBlock;
import net.minecraft.block.SeagrassBlock;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin({
	KelpBlock.class,
	KelpPlantBlock.class,
	SeagrassBlock.class,
	TallSeagrassBlock.class
})
public abstract class UnderwaterPlantMixin implements FluidFillable
{
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return method_10310(world, pos, state, fluid);
	}
	
	public boolean method_10310(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return fluid == state.getFluidState().getFluid();
	}
}
