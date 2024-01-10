package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.ReflectionUtils;

@Mixin(FlowableFluid.class)
public abstract class FlowableFluidMixin
{
	@Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
	private Block onFlowGetBlockProxy(BlockState obj, WorldAccess world, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState)
	{
		final Block block = obj.getBlock();
		final boolean fillable = block instanceof FluidFillable && ReflectionUtils.canFillWithFluid(null, (FluidFillable) block, world, pos, blockState, fluidState.getFluid());
		return fillable ? block : null;
	}
}
