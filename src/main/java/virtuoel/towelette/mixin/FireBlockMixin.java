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
	public void onGetChances(BlockState blockState_1, CallbackInfoReturnable<Integer> info)
	{
		if(FluidUtils.getFluid(blockState_1).matches(FluidTags.WATER))
		{
			info.setReturnValue(0);
		}
	}
	
	@Redirect(method = { "onScheduledTick", "trySpreadingFire" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean spreadSetBlockStateProxy(World obj, BlockPos blockPos_1, BlockState blockState_1, int int_1)
	{
		return obj.getFluidState(blockPos_1).isEmpty() ? obj.setBlockState(blockPos_1, blockState_1, int_1) : false;
	}
}
