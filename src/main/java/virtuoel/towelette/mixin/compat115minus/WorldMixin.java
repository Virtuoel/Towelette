package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import virtuoel.towelette.util.TagCompatibility;

@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow(remap = false) abstract FluidState method_8316(BlockPos pos);
	
	@Inject(method = "method_8425", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/class_1937;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;", remap = false), remap = false)
	private void doesAreaContainFireSourceGetFluidState(Box box, CallbackInfoReturnable<Boolean> info, int noop1, int noop2, int noop3, int noop4, int noop5, int noop6, @Coerce BlockPos pos, int noop7, int noop8, int noop9)
	{
		if (method_8316(pos).isIn(TagCompatibility.FluidTags.LAVA))
		{
			info.setReturnValue(true);
		}
	}
}
