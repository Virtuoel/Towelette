package virtuoel.towelette.mixin;

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
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(at = @At("RETURN"), method = "tryFillWithFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z", cancellable = true)
	private void onTryFillWithFluid(WorldAccess world, BlockPos blockPos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Boolean> info)
	{
		if (info.getReturnValue())
		{
			world.setBlockState(blockPos, FluidUtils.getStateWithFluid(blockState.with(Properties.LIT, false), fluidState), 3);
		}
		else if (FluidUtils.tryFillWithFluid(world, blockPos, blockState.with(Properties.LIT, false), fluidState))
		{
			if (blockState.get(Properties.LIT))
			{
				if (world.isClient())
				{
					boolean signal = blockState.get(Properties.SIGNAL_FIRE);
					for (int i = 0; i < 20; ++i)
					{
						CampfireBlock.spawnSmokeParticle(world.getWorld(), blockPos, signal, true);
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
