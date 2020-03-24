package virtuoel.towelette.mixin.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.util.FluidUtils;

@Mixin(StemBlock.class)
public class StemBlockMixin
{
	@Redirect(method = "method_9588", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/class_3218;method_8501(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;)Z", remap = false), remap = false)
	private boolean scheduledTickSetBlockStateProxy(ServerWorld obj, BlockPos pos, BlockState state)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos));
	}
}
