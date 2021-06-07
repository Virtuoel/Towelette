package virtuoel.towelette.util;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public interface ToweletteFluidStateExtensions extends ToweletteStateExtensions
{
	Fluid towelette_getFluid();
	boolean towelette_isStill();
	boolean towelette_isEmpty();
	VoxelShape towelette_getShape(BlockView world, BlockPos pos);
	BlockState towelette_getBlockState();
}
