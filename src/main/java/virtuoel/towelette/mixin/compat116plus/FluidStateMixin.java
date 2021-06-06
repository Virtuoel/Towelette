package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(FluidState.class)
public abstract class FluidStateMixin implements ToweletteFluidStateExtensions
{
	@Shadow abstract Fluid getFluid();
	@Shadow abstract boolean isEmpty();
	@Shadow abstract boolean isStill();
	
	@Override
	public Fluid towelette_getFluid()
	{
		return getFluid();
	}
	
	@Override
	public boolean towelette_isEmpty()
	{
		return isEmpty();
	}
	
	@Override
	public boolean towelette_isStill()
	{
		return isStill();
	}
}
