package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.world.World;
import virtuoel.towelette.api.AdditionalEntityProperties;

@Mixin(Entity.class)
@Implements(@Interface(iface = AdditionalEntityProperties.class, prefix = "towelette$", remap = Remap.NONE))
public abstract class EntityMixin
{
	@Shadow World world;
	
	@Inject(method = "checkBlockCollision", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public void checkBlockCollisionGetFluidState(CallbackInfo info, BoundingBox noop1, BlockPos.PooledMutable noop2, BlockPos.PooledMutable noop3, BlockPos.PooledMutable pos, int noop4, int noop5, int noop6)
	{
		if(world.getFluidState(pos).matches(FluidTags.LAVA))
		{
			((Entity) (Object) this).setInLava();
		}
	}
	
	@Shadow abstract boolean isInLava();
	
	@Deprecated
	public boolean towelette$isInsideLava()
	{
		return isInLava();
	}
}
