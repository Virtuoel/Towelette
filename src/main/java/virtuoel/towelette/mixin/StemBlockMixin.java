package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.util.FluidUtils;

@Mixin(StemBlock.class)
public class StemBlockMixin
{
	@Redirect(method = "scheduledTick", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
	private boolean scheduledTickSetBlockStateProxy(ServerWorld obj, BlockPos pos, BlockState state)
	{
		return obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos));
	}
}
