package virtuoel.towelette.mixin.client;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin
{
	@Inject(at = @At("HEAD"), method = "randomDisplayTick", cancellable = true)
	private void onRandomDisplayTick(World world, BlockPos pos, FluidState state, Random random, CallbackInfo info)
	{
		if (world.getBlockState(pos).isSideSolidFullSquare(world, pos, Direction.UP))
		{
			info.cancel();
		}
	}
}
