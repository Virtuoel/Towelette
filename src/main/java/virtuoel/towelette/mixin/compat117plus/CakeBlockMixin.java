package virtuoel.towelette.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin
{
	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
	private boolean onUseSetBlockStateProxy(World obj, BlockPos pos, BlockState state)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos));
	}
}
