package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(TallPlantBlock.class)
public abstract class TallPlantBlockMixin
{
	@Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean onBreakSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.setBlockState(pos, obj.getFluidState(pos).getBlockState(), flags);
	}
}
