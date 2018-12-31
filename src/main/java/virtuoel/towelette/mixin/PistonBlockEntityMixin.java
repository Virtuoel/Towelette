package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.api.FluidProperty;

@Mixin(PistonBlockEntity.class)
public abstract class PistonBlockEntityMixin
{
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean tickSetBlockStateProxy(World world, BlockPos pos, BlockState state, int flag)
	{
		if(!state.isAir())
		{
			if(state.contains(FluidProperty.FLUID) && !FluidProperty.FLUID.getFluidState(state).isEmpty())
			{
				state = state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(Fluids.EMPTY));
			}
		}
		
		return world.setBlockState(pos, state, flag);
	}
}
