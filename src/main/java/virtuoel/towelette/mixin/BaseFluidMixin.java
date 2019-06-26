package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.Towelette;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin
{
	@Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
	public Block onFlowGetBlockProxy(BlockState obj, IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, Direction direction_1, FluidState fluidState_1)
	{
		final Block block = obj.getBlock();
		final boolean fillable = block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(iWorld_1, blockPos_1, blockState_1, fluidState_1.getFluid());
		return fillable ? block : null;
	}
	
	private final ThreadLocal<Block> towelette$cachedBlock = ThreadLocal.withInitial(() -> Blocks.AIR);
	
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
	public void onMethod_15754(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1, Fluid fluid_1, CallbackInfoReturnable<Boolean> info)
	{
		final Block block = blockState_1.getBlock();
		final boolean displaceable = info.getReturnValueZ();
		final boolean fillable = block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(blockView_1, blockPos_1, blockState_1, fluid_1);
		if(!displaceable)
		{
			if(fillable)
			{
				info.setReturnValue(true);
				return;
			}
			
			final boolean empty = blockView_1.getFluidState(blockPos_1).isEmpty();
			if(empty && block.matches(Towelette.DISPLACEABLE) && !block.matches(Towelette.UNDISPLACEABLE))
			{
				info.setReturnValue(true);
				return;
			}
		}
		else if(!fillable)
		{
			final boolean empty = blockView_1.getFluidState(blockPos_1).isEmpty();
			if(!empty || block.matches(Towelette.UNDISPLACEABLE))
			{
				info.setReturnValue(false);
				return;
			}
		}
	}
}
