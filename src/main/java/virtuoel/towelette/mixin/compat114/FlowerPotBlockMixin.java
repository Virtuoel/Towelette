package virtuoel.towelette.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin
{
	@Redirect(method = "Lnet/minecraft/class_2362;method_9534(Lnet/minecraft/class_2680;Lnet/minecraft/class_1937;Lnet/minecraft/class_2338;Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;Lnet/minecraft/class_3965;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8652(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)Z", remap = false), remap = false)
	private boolean activateSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos), flags);
	}
}
