package virtuoel.towelette.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public interface FluidBlockingShapeProvider
{
	default VoxelShape getFluidBlockingShape(BlockState state, BlockView world, BlockPos pos)
	{
		return state.getCollisionShape(world, pos);
	}
}
