package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/class_2248;method_9545(Lnet/minecraft/class_2680;)Lnet/minecraft/class_3610;", cancellable = true, remap = false)
	private void onGetFluidState(BlockState state, CallbackInfoReturnable<FluidState> info)
	{
		info.setReturnValue(FluidUtils.getFluidState(state));
	}
}
