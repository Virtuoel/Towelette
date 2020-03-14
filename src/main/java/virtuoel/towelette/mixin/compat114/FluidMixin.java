package virtuoel.towelette.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import net.minecraft.world.CollisionView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.ToweletteFluidExtensions;

@Mixin(Fluid.class)
public abstract class FluidMixin implements ToweletteFluidExtensions
{
	@Shadow
	abstract int getTickRate(CollisionView world);
	
	@Override
	public int towelette_getTickRate(IWorld world)
	{
		return getTickRate(world);
	}
}
