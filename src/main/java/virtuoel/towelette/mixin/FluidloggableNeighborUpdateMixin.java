package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

@Mixin({
	DoorBlock.class,
	TrapdoorBlock.class,
	BellBlock.class,
})
public abstract class FluidloggableNeighborUpdateMixin extends BlockMixin
{
	@Override
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState state, Direction dir, BlockState var3, IWorld world, BlockPos var5, BlockPos var6, CallbackInfoReturnable<BlockState> info)
	{
		super.onGetStateForNeighborUpdate(state, dir, var3, world, var5, var6, info);
	}
}
