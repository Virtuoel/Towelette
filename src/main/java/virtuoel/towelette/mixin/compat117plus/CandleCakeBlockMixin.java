package virtuoel.towelette.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.TagCompatibility;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(CandleCakeBlock.class)
public abstract class CandleCakeBlockMixin implements FluidFillable
{
	@ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CakeBlock;tryEat(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/util/ActionResult;"))
	private BlockState onUseSetBlockStateProxy(WorldAccess obj, BlockPos pos, BlockState state, PlayerEntity player)
	{
		return FluidUtils.getStateWithFluid(state, obj, pos);
	}
	
	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState)
	{
		if (FluidUtils.tryFillWithFluid(world, pos, state, fluidState))
		{
			if (((ToweletteBlockStateExtensions) state).<Boolean>towelette_get(Properties.LIT))
			{
				AbstractCandleBlock.extinguish((PlayerEntity) null, ((ToweletteBlockStateExtensions) world.getBlockState(pos)).towelette_with(Properties.LIT, false).towelette_cast(), world, pos);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Inject(at = @At("HEAD"), method = "canBeLit", cancellable = true)
	private static void onCanBeLit(BlockState state, CallbackInfoReturnable<Boolean> info)
	{
		if (TagCompatibility.isIn(FluidUtils.getFluid(state), TagCompatibility.FluidTags.WATER))
		{
			info.setReturnValue(false);
		}
	}
}
