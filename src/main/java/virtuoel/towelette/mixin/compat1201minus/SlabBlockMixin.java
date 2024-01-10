package virtuoel.towelette.mixin.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import virtuoel.towelette.util.FluidUtils;

@Mixin(value = SlabBlock.class, priority = 999)
public abstract class SlabBlockMixin
{
	@Redirect(method = "method_10310", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3737;method_10310(Lnet/minecraft/class_1922;Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_3611;)Z", remap = false), remap = false)
	private boolean canFillWithFluidProxy(Waterloggable obj, BlockView world, BlockPos blockPos, BlockState blockState, Fluid fluid)
	{
		return FluidUtils.canFillWithFluid(world, blockPos, blockState, fluid);
	}
}
