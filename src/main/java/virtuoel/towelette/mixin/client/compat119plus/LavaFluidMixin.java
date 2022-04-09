package virtuoel.towelette.mixin.client.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.AbstractRandom;
import net.minecraft.world.World;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin
{
	@Inject(at = @At("HEAD"), method = "randomDisplayTick", cancellable = true)
	private void onRandomDisplayTick(World world, BlockPos pos, FluidState state, AbstractRandom random, CallbackInfo info)
	{
		if (((ToweletteBlockStateExtensions) world.getBlockState(pos)).towelette_isSideSolidFullSquare(world, pos, Direction.UP))
		{
			info.cancel();
		}
	}
}
