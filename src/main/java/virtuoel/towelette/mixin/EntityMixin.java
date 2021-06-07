package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import virtuoel.towelette.api.CollidableFluid;
import virtuoel.towelette.util.ToweletteEntityExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(Entity.class)
public abstract class EntityMixin implements ToweletteEntityExtensions
{
	@Shadow World world;
	
	@Inject(method = "checkBlockCollision", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private void checkBlockCollisionGetFluidState(CallbackInfo info, Box noop1, @Coerce BlockPos noop2, @Coerce BlockPos noop3, @Coerce BlockPos pos, int noop4, int noop5, int noop6)
	{
		final FluidState fluidState = world.getFluidState(pos);
		final ToweletteFluidStateExtensions state = (ToweletteFluidStateExtensions) (Object) fluidState;
		
		((CollidableFluid) state.towelette_getFluid()).onEntityCollision(fluidState, world, pos, (Entity) (Object) this);
	}
}
