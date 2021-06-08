package virtuoel.towelette.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.TagCompatibility;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(CandleBlock.class)
public class CandleBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		if (((ToweletteFluidStateExtensions) (Object) context.getWorld().getFluidState(context.getBlockPos())).towelette_isIn(TagCompatibility.FluidTags.WATER))
		{
			info.setReturnValue(((ToweletteBlockStateExtensions) info.getReturnValue()).towelette_with(Properties.LIT, false).towelette_cast());
		}
	}
	
	@Inject(at = @At("RETURN"), method = "tryFillWithFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z", cancellable = true)
	private void onTryFillWithFluid(WorldAccess world, BlockPos blockPos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Boolean> info)
	{
		final ToweletteBlockStateExtensions state = (ToweletteBlockStateExtensions) blockState;
		if (info.getReturnValue())
		{
			world.setBlockState(blockPos, FluidUtils.getStateWithFluid(state.towelette_with(Properties.LIT, false).towelette_cast(), fluidState), 3);
		}
		else if (FluidUtils.tryFillWithFluid(world, blockPos, state.towelette_with(Properties.LIT, false).towelette_cast(), fluidState))
		{
			if (state.<Boolean>towelette_get(Properties.LIT))
			{
				AbstractCandleBlock.extinguish(null, blockState, world, blockPos);
			}
			
			info.setReturnValue(true);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "canBeLit", cancellable = true)
	private static void onCanBeLit(BlockState state, CallbackInfoReturnable<Boolean> info)
	{
		if (FluidUtils.getFluid(state).isIn(TagCompatibility.FluidTags.WATER))
		{
			info.setReturnValue(false);
		}
	}
}
