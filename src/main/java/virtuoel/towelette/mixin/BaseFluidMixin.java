package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.Towelette;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin
{
	@Shadow abstract void method_15730(IWorld var1, BlockPos var2, BlockState var3);
	
	@Inject(at = @At("HEAD"), method = "method_15745", cancellable = true)
	public void onMethod_15745(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, Direction direction_1, FluidState fluidState_1, CallbackInfo info)
	{
		boolean filled = !iWorld_1.getFluidState(blockPos_1).isEmpty();
		if(blockState_1.getBlock() instanceof FluidFillable)
		{
			filled |= ((FluidFillable) blockState_1.getBlock()).tryFillWithFluid(iWorld_1, blockPos_1, blockState_1, fluidState_1);
		}
		
		if(!filled && (blockState_1.isAir() || (blockState_1.getBlock().matches(Towelette.DISPLACEABLE) && !blockState_1.getBlock().matches(Towelette.UNDISPLACEABLE))))
		{
			if(!blockState_1.isAir())
			{
				this.method_15730(iWorld_1, blockPos_1, blockState_1);
			}
			
			iWorld_1.setBlockState(blockPos_1, fluidState_1.getBlockState(), 3);
		}
		
		info.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "method_15754", cancellable = true)
	public void onMethod_15754(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1, Fluid fluid_1, CallbackInfoReturnable<Boolean> info)
	{
		if(blockView_1.getFluidState(blockPos_1).isEmpty())
		{
			if(blockState_1.isAir() || (blockState_1.getBlock().matches(Towelette.DISPLACEABLE) && !blockState_1.getBlock().matches(Towelette.UNDISPLACEABLE)))
			{
				info.setReturnValue(true);
			}
		}
	}
}
