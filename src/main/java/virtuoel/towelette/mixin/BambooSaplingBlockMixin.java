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
	public boolean getStateForNeighborUpdateSetBlockStateProxy(IWorld obj, BlockPos blockPos_1, BlockState blockState_1, int flags)
	{
		return obj.setBlockState(blockPos_1, FluidUtils.getStateWithFluid(blockState_1, obj, blockPos_1), flags);
	}
	
	@Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2, CallbackInfoReturnable<BlockState> info)
	{
		info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), iWorld_1, blockPos_1));
	}
}
