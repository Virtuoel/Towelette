package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.api.CollidableFluid;
import virtuoel.towelette.util.TagCompatibility;

@Mixin(Fluid.class)
public class FluidMixin implements CollidableFluid
{
	@Override
	public void onEntityCollision(FluidState state, World world, BlockPos pos, Entity entity)
	{
		if (state.matches(TagCompatibility.FluidTags.LAVA))
		{
			entity.setInLava();
		}
	}
}
