package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

@Mixin(BlockState.class)
public abstract class BlockStateMixin
{
	@Shadow int luminance;
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
}
