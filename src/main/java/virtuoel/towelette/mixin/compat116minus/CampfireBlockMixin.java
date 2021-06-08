package virtuoel.towelette.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(at = @At("RETURN"), method = "method_10311(Lnet/minecraft/class_1936;Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_3610;)Z", cancellable = true, remap = false)
	private void onTryFillWithFluid(WorldAccess world, BlockPos blockPos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Boolean> info)
	{
		final ToweletteBlockStateExtensions state = (ToweletteBlockStateExtensions) blockState;
		if (info.getReturnValue())
		{
			world.setBlockState(blockPos, FluidUtils.getStateWithFluid(state.towelette_with(Properties.LIT, false).towelette_cast(), fluidState), 3);
		}
		else if (FluidUtils.tryFillWithFluid(world, blockPos, state.towelette_with(Properties.LIT, false).towelette_cast(), fluidState))
		{
			if (state.<Boolean>towelette_get(Properties.LIT))
			{
				if (world.isClient())
				{
					if (world instanceof World)
					{
						final boolean signal = state.towelette_get(Properties.SIGNAL_FIRE);
						for (int i = 0; i < 20; ++i)
						{
							CampfireBlock.spawnSmokeParticle((World) world, blockPos, signal, true);
						}
					}
				}
				else
				{
					world.playSound(null, blockPos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			info.setReturnValue(true);
		}
	}
}
