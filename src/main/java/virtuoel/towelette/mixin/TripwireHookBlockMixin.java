package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(TripwireHookBlock.class)
public class TripwireHookBlockMixin
{
	@Redirect(method = "method_10776", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean method_10776SetBlockStateProxy(World obj, BlockPos blockPos_1, BlockState blockState_1, int int_1)
	{
		return obj.setBlockState(blockPos_1, FluidUtils.getStateWithFluid(blockState_1, obj, blockPos_1), int_1);
	}
}
