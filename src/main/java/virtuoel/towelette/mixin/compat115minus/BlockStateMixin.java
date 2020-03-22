package virtuoel.towelette.mixin.compat115minus;

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
	@Shadow(remap = false) abstract FluidState method_11618();
	
	@Inject(at = @At("RETURN"), method = "method_11630", cancellable = true, remap = false)
	private void onGetLuminance(CallbackInfoReturnable<Integer> info)
	{
		final FluidState fluidState = method_11618();
		
		if (fluidState.getFluid() != Fluids.EMPTY)
		{
			final BlockState fluidBlockState = fluidState.getBlockState();
			
			if (fluidBlockState != (BlockState) (Object) this)
			{
				info.setReturnValue(Math.max(info.getReturnValue(), fluidBlockState.getLuminance()));
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "method_11578", remap = false)
	private void onGetStateForNeighborUpdate(Direction direction, BlockState blockState, IWorld world, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, pos);
	}
	
	@Inject(at = @At("RETURN"), method = "method_11580", remap = false)
	private void onOnBlockAdded(World world, BlockPos blockPos, BlockState blockState, boolean flag, CallbackInfo info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, blockPos);
	}
}
