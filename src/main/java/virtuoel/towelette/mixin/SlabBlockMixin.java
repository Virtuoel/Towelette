package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(value = SlabBlock.class, priority = 999)
public abstract class SlabBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		if (((ToweletteBlockStateExtensions) context.getWorld().getBlockState(context.getBlockPos())).towelette_getBlock() == (SlabBlock) (Object) this)
		{
			final BlockState state = FluidUtils.getStateWithFluid(info.getReturnValue(), Fluids.EMPTY.getDefaultState());
			if (state != null)
			{
				info.setReturnValue(state);
			}
		}
	}
	
	@Redirect(method = "tryFillWithFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Waterloggable;tryFillWithFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z"))
	private boolean tryFillWithFluidProxy(Waterloggable obj, WorldAccess world, BlockPos blockPos, BlockState blockState, FluidState fluidState)
	{
		return FluidUtils.tryFillWithFluid(world, blockPos, blockState, fluidState);
	}
}
