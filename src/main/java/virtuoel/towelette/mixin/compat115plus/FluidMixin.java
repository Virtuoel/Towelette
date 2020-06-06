package virtuoel.towelette.mixin.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldView;
import virtuoel.towelette.util.ToweletteFluidExtensions;

@Mixin(Fluid.class)
public abstract class FluidMixin implements ToweletteFluidExtensions
{
	@Shadow
	abstract int getTickRate(WorldView world);
	
	@Override
	public int towelette_getTickRate(IWorld world)
	{
		return getTickRate(world);
	}
}
