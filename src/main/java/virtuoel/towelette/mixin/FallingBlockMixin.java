package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FallingBlock.class)
public class FallingBlockMixin
{
	@Redirect(method = "tryStartFalling", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public BlockState tryStartFallingGetBlockStateProxy(World obj, BlockPos blockPos_1)
	{
		return FluidUtils.getStateWithFluid(obj.getBlockState(blockPos_1), Fluids.EMPTY);
	}
}
