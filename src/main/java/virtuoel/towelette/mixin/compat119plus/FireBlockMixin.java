package virtuoel.towelette.mixin.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(FireBlock.class)
public class FireBlockMixin
{
	@Redirect(method = { "scheduledTick", "trySpreadingFire" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean spreadSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return ((ToweletteFluidStateExtensions) (Object) obj.getFluidState(pos)).towelette_isEmpty() ? obj.setBlockState(pos, state, flags) : false;
	}
}
