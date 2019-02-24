package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.hooks.FluidloggableHooks;

@Mixin(BlockState.class)
public abstract class BlockStateMixin
{
	@Shadow abstract FluidState getFluidState();
	
	@Inject(at = @At("RETURN"), method = "getLuminance", cancellable = true)
	public void onGetLuminance(CallbackInfoReturnable<Integer> info)
	{
		FluidState fluidState = getFluidState();
		if(fluidState.getFluid() != Fluids.EMPTY)
		{
			BlockState fluidBlockState = fluidState.getBlockState();
			if(fluidBlockState != (BlockState) (Object) this)
			{
				info.setReturnValue(Math.max(info.getReturnValue(), fluidBlockState.getLuminance()));
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate")
	public void onGetStateForNeighborUpdate(Direction direction_1, BlockState blockState_1, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookScheduleFluidTick((BlockState) (Object) this, iWorld_1, blockPos_1);
	}
}
