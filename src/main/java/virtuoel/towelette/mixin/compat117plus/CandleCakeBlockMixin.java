package virtuoel.towelette.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.TagCompatibility;

@Mixin(CandleCakeBlock.class)
public abstract class CandleCakeBlockMixin
{
	@ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CakeBlock;tryEat(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/util/ActionResult;"))
	private BlockState onUseSetBlockStateProxy(WorldAccess obj, BlockPos pos, BlockState state, PlayerEntity player)
	{
		return FluidUtils.getStateWithFluid(state, obj, pos);
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
