package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(TallPlantBlock.class)
public abstract class TallPlantBlockMixin
{
	@Redirect(method = "method_9576", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", remap = true), remap = false)
	private boolean onBreakSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.setBlockState(pos, ((ToweletteFluidStateExtensions) (Object) obj.getFluidState(pos)).towelette_getBlockState(), flags);
	}
}
