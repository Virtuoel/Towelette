package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.fluid.Fluids;
import virtuoel.towelette.util.FluidUtils;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin
{
	@ModifyArg(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private BlockState teleportSetBlockStateModified(BlockState blockState)
	{
		return FluidUtils.getStateWithFluid(blockState, Fluids.EMPTY.getDefaultState());
	}
}
