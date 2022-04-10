package virtuoel.towelette.mixin.compat118minus;

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
	@Redirect(method = { "method_9588", "method_10196" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8652(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)Z"))
	private boolean spreadSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return ((ToweletteFluidStateExtensions) (Object) obj.getFluidState(pos)).towelette_isEmpty() ? obj.setBlockState(pos, state, flags) : false;
	}
}
