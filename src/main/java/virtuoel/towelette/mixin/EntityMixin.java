package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.Tag;
import virtuoel.towelette.api.AdditionalEntityProperties;

@Mixin(Entity.class)
public abstract class EntityMixin implements AdditionalEntityProperties
{
	@Shadow abstract boolean isInsideFluid(Tag<Fluid> tag_1);
	
	protected boolean insideLava = false;
	
	@Inject(at = @At("HEAD"), method = "method_5876")
	public void onMethod_5876(CallbackInfo info)
	{
		this.insideLava = isInsideFluid(FluidTags.LAVA);
	}
	
	@Override
	public boolean isInsideLava()
	{
		return this.insideLava;
	}
	
	@Inject(at = @At("RETURN"), method = "isTouchingLava", cancellable = true)
	public void onIsTouchingLava(CallbackInfoReturnable<Boolean> info)
	{
		info.setReturnValue(info.getReturnValue() || isInsideLava());
	}
}
