package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FireBlock.class)
public class FireBlockMixin
{
	@Inject(at = @At("HEAD"), method = { "getSpreadChance", "getBurnChance" }, cancellable = true)
	private void onGetChances(BlockState state, CallbackInfoReturnable<Integer> info)
	{
		if(FluidUtils.getFluid(state).matches(FluidTags.WATER))
		{
			info.setReturnValue(0);
		}
	}
	
	@Redirect(method = { "scheduledTick", "trySpreadingFire" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean spreadSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.getFluidState(pos).isEmpty() ? obj.setBlockState(pos, state, flags) : false;
	}
}
