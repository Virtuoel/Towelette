package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(CropBlock.class)
public class CropBlockMixin
{
	@Redirect(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean scheduledTickSetBlockStateProxy(ServerWorld obj, BlockPos blockPos, BlockState blockState, int flags)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos), flags);
	}
	
	@Redirect(method = "applyGrowth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean applyGrowthSetBlockStateProxy(World obj, BlockPos blockPos, BlockState blockState, int flags)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos), flags);
	}
}
