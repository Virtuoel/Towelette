package virtuoel.towelette.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import net.minecraft.world.CollisionView;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.ToweletteFluidExtensions;

@Mixin(Fluid.class)
public abstract class FluidMixin implements ToweletteFluidExtensions
{
	@Shadow(remap = false)
	abstract int method_15789(CollisionView world);
	
	@Override
	public int towelette_getTickRate(WorldAccess world)
	{
		return method_15789(world);
	}
}
