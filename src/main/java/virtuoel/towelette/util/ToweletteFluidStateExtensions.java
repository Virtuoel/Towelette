package virtuoel.towelette.util;

import net.minecraft.fluid.Fluid;

public interface ToweletteFluidStateExtensions
{
	Fluid towelette_getFluid();
	boolean towelette_isStill();
	boolean towelette_isEmpty();
}
