package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(TallPlantBlock.class)
public abstract class TallPlantMixin
{
	@Redirect(method = "method_30036", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private static boolean onBreakSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.setBlockState(pos, obj.getFluidState(pos).getBlockState(), flags);
	}
	
	@Redirect(method = { "onPlaced", "placeAt" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean placement_growth_SetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos), flags);
	}
}
