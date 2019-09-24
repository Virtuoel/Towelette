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
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(at = @At("RETURN"), method = "tryFillWithFluid(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z", cancellable = true)
	public void onTryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, FluidState fluidState_1, CallbackInfoReturnable<Boolean> info)
	{
		if(info.getReturnValue())
		{
			iWorld_1.setBlockState(blockPos_1, FluidUtils.getStateWithFluid(blockState_1.with(Properties.LIT, false), fluidState_1), 3);
		}
		else if(FluidUtils.tryFillWithFluid(iWorld_1, blockPos_1, blockState_1.with(Properties.LIT, false), fluidState_1))
		{
			if(blockState_1.get(Properties.LIT))
			{
				if(iWorld_1.isClient())
				{
					boolean signal = blockState_1.get(Properties.SIGNAL_FIRE);
					for(int int_1 = 0; int_1 < 20; ++int_1)
					{
						CampfireBlock.spawnSmokeParticle(iWorld_1.getWorld(), blockPos_1, signal, true);
					}
				}
				else
				{
					iWorld_1.playSound(null, blockPos_1, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			info.setReturnValue(true);
		}
	}
}
