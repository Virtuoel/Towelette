package virtuoel.towelette.mixin.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AbstractPlantStemBlock.class)
public abstract class AbstractPlantStemBlockMixin
{
	@Redirect(method = "grow", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
	private boolean growSetBlockStateProxy(ServerWorld obj, BlockPos blockPos, BlockState blockState)
	{
		return obj.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState, obj, blockPos));
	}
}
