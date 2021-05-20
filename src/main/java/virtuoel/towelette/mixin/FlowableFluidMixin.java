package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import virtuoel.towelette.Towelette;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin
{
	@Redirect(method = "receivesFlow", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/shape/VoxelShapes;adjacentSidesCoverSquare(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/math/Direction;)Z"))
	private boolean receivesFlowAdjacentSidesCoverSquareProxy(VoxelShape shape, VoxelShape fromShape, Direction face, Direction noop, BlockView world, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState)
	{
		return FluidUtils.isFluidFlowBlocked(face, world, shape, state, pos, fromShape, fromState, fromPos);
	}
	
	@Unique private final ThreadLocal<Block> towelette$cachedBlock = ThreadLocal.withInitial(() -> Blocks.AIR);
	
	@ModifyVariable(method = "canFill", at = @At(value = "LOAD", ordinal = 0), allow = 1, require = 1)
	private Block hookCanFillNotFillable(Block block)
	{
		towelette$cachedBlock.set(block);
		return null;
	}
	
	@ModifyVariable(method = "canFill", at = @At(value = "LOAD", ordinal = 2), allow = 1, require = 1)
	private Block hookCanFillRevertBlock(Block block)
	{
		final Block cache = towelette$cachedBlock.get();
		towelette$cachedBlock.remove();
		return cache;
	}
	
	@Inject(at = @At("RETURN"), method = "canFill", cancellable = true)
	private void onCanFill(BlockView blockView, BlockPos pos, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> info)
	{
		final Block block = ((ToweletteBlockStateExtensions) state).towelette_getBlock();
		
		if (!info.getReturnValueZ())
		{
			if ((block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(blockView, pos, state, fluid)) || (state.isIn(Towelette.DISPLACEABLE) && !state.isIn(Towelette.UNDISPLACEABLE) && blockView.getFluidState(pos).isEmpty()))
			{
				info.setReturnValue(true);
			}
		}
		else if (block instanceof FluidFillable && (state.isIn(Towelette.UNDISPLACEABLE) || (!(block instanceof FluidBlock) && !blockView.getFluidState(pos).isEmpty())))
		{
			info.setReturnValue(false);
		}
	}
	
	@Redirect(method = "onScheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean onOnScheduledTickSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags, World world, BlockPos blockPos, FluidState fluidState)
	{
		final BlockState existingState = world.getBlockState(pos);
		final BlockState stateWithFluid = FluidUtils.getStateWithFluid(existingState, state != Blocks.AIR.getDefaultState() ? fluidState : Fluids.EMPTY.getDefaultState());
		return obj.setBlockState(pos, existingState == stateWithFluid ? state : stateWithFluid, flags);
	}
}
