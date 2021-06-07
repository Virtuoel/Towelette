package virtuoel.towelette.mixin.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import virtuoel.towelette.util.ToweletteFluidExtensions;

@Mixin(Fluid.class)
public abstract class FluidMixin implements ToweletteFluidExtensions
{
	@Shadow
	abstract int getTickRate(WorldView world);
	
	@Override
	public int towelette_getTickRate(WorldAccess world)
	{FluidTags.LAVA.getClass();
		return getTickRate(world);
	}
}
