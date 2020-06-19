package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin
{
	@Inject(at = @At("HEAD"), method = "neighborUpdate")
	private void onNeighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos otherPos, boolean unknown, CallbackInfo info)
	{
		boolean powered = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.offset(blockState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if (!world.isClient && powered != blockState.get(Properties.POWERED))
		{
			FluidUtils.scheduleFluidTick(blockState, world, blockPos);
		}
	}
	
	@Redirect(method = "onPlaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean onPlacedSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos), flags);
	}
}
