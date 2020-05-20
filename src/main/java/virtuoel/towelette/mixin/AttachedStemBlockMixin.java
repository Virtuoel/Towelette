package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AttachedStemBlock.class)
public class AttachedStemBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate", cancellable = true)
	private void onGetStateForNeighborUpdate(BlockState state, Direction direction, BlockState otherState, WorldAccess world, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info)
	{
		info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), FluidUtils.getFluidState(state)));
	}
}
