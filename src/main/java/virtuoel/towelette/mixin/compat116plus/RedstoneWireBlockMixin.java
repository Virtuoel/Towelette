package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import virtuoel.towelette.util.FluidUtils;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getDefaultWireState", cancellable = true)
	private void onGetDefaultWireState(BlockView blockView, BlockState blockState, BlockPos blockPos, CallbackInfoReturnable<BlockState> info)
	{
		final FluidState state = blockView.getFluidState(blockPos);
		
		if (!state.isEmpty())
		{
			info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), state));
		}
	}
}
