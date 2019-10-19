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
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import virtuoel.towelette.Towelette;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin
{
	@Redirect(method = "receivesFlow", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/shape/VoxelShapes;method_1080(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/math/Direction;)Z"))
	private boolean receivesFlowMethod_1080Proxy(VoxelShape shape, VoxelShape otherShape, Direction direction, Direction noop, BlockView world, BlockPos blockPos, BlockState blockState, BlockPos otherPos, BlockState otherState)
	{
		return FluidUtils.isFluidFlowBlocked(direction, world, shape, blockState, blockPos, otherShape, otherState, otherPos);
	}
	
	@Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
	private Block onFlowGetBlockProxy(BlockState obj, IWorld world, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState)
	{
		final Block block = obj.getBlock();
		final boolean fillable = block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(world, pos, blockState, fluidState.getFluid());
		return fillable ? block : null;
	}
	
	@Unique private final ThreadLocal<Block> towelette$cachedBlock = ThreadLocal.withInitial(() -> Blocks.AIR);
	
	@ModifyVariable(method = "method_15754", at = @At(value = "LOAD", ordinal = 0), allow = 1, require = 1)
	private Block hookMethod_15754NotFillable(Block block)
	{
		towelette$cachedBlock.set(block);
		return null;
	}
	
	@ModifyVariable(method = "method_15754", at = @At(value = "LOAD", ordinal = 2), allow = 1, require = 1)
	private Block hookMethod_15754RevertBlock(Block block)
	{
		final Block cache = towelette$cachedBlock.get();
		towelette$cachedBlock.remove();
		return cache;
	}
	
	@Inject(at = @At("RETURN"), method = "method_15754", cancellable = true)
	private void onMethod_15754(BlockView blockView, BlockPos pos, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> info)
	{
		final Block block = state.getBlock();
		final boolean displaceable = info.getReturnValueZ();
		final boolean fillable = block instanceof FluidFillable;
		final boolean canFill = fillable && ((FluidFillable) block).canFillWithFluid(blockView, pos, state, fluid);
		if(!displaceable)
		{
			if(canFill)
			{
				info.setReturnValue(true);
				return;
			}
			
			final boolean empty = blockView.getFluidState(pos).isEmpty();
			if(empty && block.matches(Towelette.DISPLACEABLE) && !block.matches(Towelette.UNDISPLACEABLE))
			{
				info.setReturnValue(true);
				return;
			}
		}
		else if(fillable && !canFill)
		{
			final boolean empty = blockView.getFluidState(pos).isEmpty();
			if(!empty || block.matches(Towelette.UNDISPLACEABLE))
			{
				info.setReturnValue(false);
				return;
			}
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
