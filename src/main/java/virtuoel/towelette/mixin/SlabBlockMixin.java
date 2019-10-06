package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

@Mixin(value = SlabBlock.class, priority = 999)
public abstract class SlabBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() == (SlabBlock) (Object) this)
		{
			final BlockState state = FluidUtils.getStateWithFluid(info.getReturnValue(), Fluids.EMPTY.getDefaultState());
			if(state != null)
			{
				info.setReturnValue(state);
			}
		}
	}
	
	@Redirect(method = "canFillWithFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Waterloggable;canFillWithFluid(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/Fluid;)Z"))
	private boolean canFillWithFluidProxy(Waterloggable obj, BlockView world, BlockPos blockPos, BlockState blockState, Fluid fluid)
	{
		return FluidUtils.canFillWithFluid(world, blockPos, blockState, fluid);
	}
	
	@Redirect(method = "tryFillWithFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Waterloggable;tryFillWithFluid(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z"))
	private boolean tryFillWithFluidProxy(Waterloggable obj, IWorld world, BlockPos blockPos, BlockState blockState, FluidState fluidState)
	{
		return FluidUtils.tryFillWithFluid(world, blockPos, blockState, fluidState);
	}
}
