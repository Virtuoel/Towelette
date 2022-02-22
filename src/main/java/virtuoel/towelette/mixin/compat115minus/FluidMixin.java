package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import virtuoel.towelette.api.CollidableFluid;
import virtuoel.towelette.util.TagCompatibility;
import virtuoel.towelette.util.ToweletteEntityExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(Fluid.class)
public class FluidMixin implements CollidableFluid
{
	@Override
	public void onEntityCollision(FluidState state, World world, BlockPos pos, Entity entity)
	{
		if (TagCompatibility.isIn(state, TagCompatibility.FluidTags.LAVA))
		{
			final double f = (float) pos.getY() + ((ToweletteFluidStateExtensions) (Object) state).towelette_getHeight(world, pos);
			final Box bounds = entity.getBoundingBox();
			
			if (bounds.minY < f || f > bounds.maxY)
			{
				((ToweletteEntityExtensions) entity).towelette_setInLava();
			}
		}
	}
}
