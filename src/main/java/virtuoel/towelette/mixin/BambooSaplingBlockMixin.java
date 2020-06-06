package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BambooSaplingBlock.class)
public class BambooSaplingBlockMixin
{
	@Redirect(method = "getStateForNeighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean getStateForNeighborUpdateSetBlockStateProxy(IWorld obj, BlockPos blockPos, BlockState blockState, int flags)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos), flags);
	}
	
	@Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", cancellable = true)
	private void onGetStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState otherState, IWorld world, BlockPos blockPos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info)
	{
		info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), world, blockPos));
	}
}
