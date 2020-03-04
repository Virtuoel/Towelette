package virtuoel.towelette.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(CropBlock.class)
public class CropBlockMixin
{
	@Redirect(method = "Lnet/minecraft/class_2302;method_9588(Lnet/minecraft/class_2680;Lnet/minecraft/class_1937;Lnet/minecraft/class_2338;Ljava/util/Random;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8652(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)Z", remap = false), remap = false)
	private boolean scheduledTickSetBlockStateProxy(World obj, BlockPos blockPos, BlockState blockState, int flags)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos), flags);
	}
}
