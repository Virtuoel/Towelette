package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(at = @At("RETURN"), method = "tryFillWithFluid(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/FluidState;)Z", cancellable = true)
	public void onTryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, FluidState fluidState_1, CallbackInfoReturnable<Boolean> info)
	{
		if(info.getReturnValue())
		{
			iWorld_1.setBlockState(blockPos_1, blockState_1.with(Properties.WATERLOGGED, true).with(Properties.LIT, false).with(FluidProperty.FLUID, FluidProperty.FLUID.of(Fluids.WATER)), 3);
		}
		else
		{
			info.setReturnValue(Fluidloggable.fillImpl(iWorld_1, blockPos_1, blockState_1, fluidState_1));
		}
	}
}
