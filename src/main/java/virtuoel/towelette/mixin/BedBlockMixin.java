package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin
{
	@Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean onBreakSetBlockStateProxy(World obj, BlockPos blockPos_1, BlockState blockState_1, int int_1)
	{
		return obj.setBlockState(blockPos_1, obj.getFluidState(blockPos_1).getBlockState(), int_1);
	}
	
	@Redirect(method = "onPlaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean onPlacedSetBlockStateProxy(World obj, BlockPos blockPos_1, BlockState blockState_1, int int_1)
	{
		return obj.setBlockState(blockPos_1, FluidUtils.getStateWithFluid(blockState_1, obj, blockPos_1), int_1);
	}
}
