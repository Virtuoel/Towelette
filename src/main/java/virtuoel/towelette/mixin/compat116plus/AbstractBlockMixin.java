package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin
{
	@Inject(at = @At("HEAD"), method = "getFluidState", cancellable = true)
	private void onGetFluidState(BlockState state, CallbackInfoReturnable<FluidState> info)
	{
		info.setReturnValue(FluidUtils.getFluidState(state));
	}
}
