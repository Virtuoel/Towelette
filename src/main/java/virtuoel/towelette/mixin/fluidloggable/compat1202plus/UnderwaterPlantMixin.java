package virtuoel.towelette.mixin.fluidloggable.compat1202plus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.KelpBlock;
import net.minecraft.block.KelpPlantBlock;
import net.minecraft.block.SeagrassBlock;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.entity.player.PlayerEntity;
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
	@Override
	public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return fluid == state.getFluidState().getFluid();
	}
}
