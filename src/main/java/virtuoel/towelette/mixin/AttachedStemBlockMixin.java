package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AttachedStemBlock.class)
public class AttachedStemBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2, CallbackInfoReturnable<BlockState> info)
	{
		info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), FluidProperty.FLUID.getFluidState(blockState_1)));
	}
}
