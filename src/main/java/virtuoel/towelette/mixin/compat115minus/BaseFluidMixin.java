package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(BaseFluid.class)
public abstract class BaseFluidMixin
{
	@Redirect(method = "method_15745", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2680;method_11614()Lnet/minecraft/class_2248;", remap = false), remap = false)
	private Block onFlowGetBlockProxy(BlockState obj, IWorld world, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState)
	{
		final Block block = ((ToweletteBlockStateExtensions) obj).towelette_getBlock();
		final boolean fillable = block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(world, pos, blockState, fluidState.getFluid());
		return fillable ? block : null;
	}
}
