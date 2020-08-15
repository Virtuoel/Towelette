package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.fluid.Fluid;
import virtuoel.towelette.api.CollidableFluid;

@Mixin(Fluid.class)
public class FluidMixin implements CollidableFluid
{
	
}
