package virtuoel.towelette.mixin.compat116plus.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AbstractPlantStemBlock.class)
public abstract class AbstractPlantStemBlockMixin
{
	@Redirect(method = "method_9652", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3218;method_8501(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;)Z", remap = false), remap = false)
	private boolean growSetBlockStateProxy(ServerWorld obj, BlockPos blockPos, BlockState blockState)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos));
	}
}
