package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(targets = "net.minecraft.class_3610", remap = false)
public interface FluidStateMixin extends ToweletteFluidStateExtensions
{
	@Shadow(remap = false) Fluid method_15772();
	@Shadow(remap = false) boolean method_15769();
	@Shadow(remap = false) boolean method_15771();
	
	@Override
	default Fluid towelette_getFluid()
	{
		return method_15772();
	}
	
	@Override
	default boolean towelette_isEmpty()
	{
		return method_15769();
	}
	
	@Override
	default boolean towelette_isStill()
	{
		return method_15771();
	}
}
