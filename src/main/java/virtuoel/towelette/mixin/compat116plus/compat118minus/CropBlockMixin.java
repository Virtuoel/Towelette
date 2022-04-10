package virtuoel.towelette.mixin.compat116plus.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.util.FluidUtils;

@Mixin(CropBlock.class)
public class CropBlockMixin
{
	@Redirect(method = "method_9514", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3218;method_8652(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)Z", remap = false), remap = false)
	private boolean randomTickSetBlockStateProxy(ServerWorld obj, BlockPos blockPos, BlockState blockState, int flags)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos), flags);
	}
}
