package virtuoel.towelette.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(StemBlock.class)
public class StemBlockMixin
{
	@Redirect(method = "Lnet/minecraft/class_2513;method_9588(Lnet/minecraft/class_2680;Lnet/minecraft/class_1937;Lnet/minecraft/class_2338;Ljava/util/Random;)V", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/class_1937;method_8501(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;)Z", remap = false), remap = false)
	private boolean scheduledTickSetBlockStateProxy(World obj, BlockPos pos, BlockState state)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos));
	}
}
