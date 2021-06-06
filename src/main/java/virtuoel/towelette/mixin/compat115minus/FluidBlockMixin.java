package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

@Mixin(FluidBlock.class)
public class FluidBlockMixin
{
	@Inject(method = "method_9548", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_20447()V", remap = false), remap = false, cancellable = true)
	private void onOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info)
	{
		final double f = (float) pos.getY() + world.getFluidState(pos).getHeight(world, pos);
		final Box bounds = entity.getBoundingBox();
		
		if (bounds.minY >= f || f <= bounds.maxY)
		{
			info.cancel();
		}
	}
}
