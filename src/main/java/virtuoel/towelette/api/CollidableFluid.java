package virtuoel.towelette.api;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CollidableFluid
{
	default void onEntityCollision(FluidState state, World world, BlockPos pos, Entity entity)
	{
		if (state.matches(FluidTags.LAVA))
		{
			entity.setInLava();
		}
	}
}
