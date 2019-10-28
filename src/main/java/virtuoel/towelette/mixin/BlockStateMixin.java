package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BlockState.class)
public abstract class BlockStateMixin
{
	@Shadow abstract FluidState getFluidState();
	
	@Inject(at = @At("RETURN"), method = "getLuminance", cancellable = true)
	private void onGetLuminance(CallbackInfoReturnable<Integer> info)
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
	
	@Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate")
	private void onGetStateForNeighborUpdate(Direction direction, BlockState blockState, IWorld world, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, pos);
	}
	
	@Inject(at = @At("RETURN"), method = "onBlockAdded")
	private void onOnBlockAdded(World world, BlockPos blockPos, BlockState blockState, boolean flag, CallbackInfo info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, blockPos);
	}
}
