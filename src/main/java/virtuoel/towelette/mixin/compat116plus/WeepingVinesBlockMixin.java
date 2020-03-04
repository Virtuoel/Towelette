package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

@Mixin(WeepingVinesBlock.class)
public class WeepingVinesBlockMixin
{
	@Inject(method = "getStateForNeighborUpdate", at = @At("RETURN"), cancellable = true)
	private void onGetStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> info)
	{
		if (facing == Direction.DOWN && neighborState.getBlock() == (Object) this)
		{
			info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), world, pos));
		}
	}
}
