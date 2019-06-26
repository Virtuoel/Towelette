package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow abstract FluidState getFluidState(BlockPos blockPos_1);
	
	@Inject(method = "doesAreaContainFireSource", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true, at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public void doesAreaContainFireSourceGetFluidState(Box b, CallbackInfoReturnable<Boolean> info, int noop1, int noop2, int noop3, int noop4, int noop5, int noop6, BlockPos.PooledMutable pos, int noop7, int noop8, int noop9)
	{
		if(getFluidState(pos).matches(FluidTags.LAVA))
		{
			info.setReturnValue(true);
		}
	}
}
