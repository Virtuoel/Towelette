package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getLandingState", cancellable = true)
	private static void onGetLandingState(BlockState blockState, CallbackInfoReturnable<BlockState> info)
	{
		info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), FluidUtils.getFluidState(blockState)));
	}
}
