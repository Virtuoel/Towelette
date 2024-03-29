package virtuoel.towelette.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface ToweletteBlockStateExtensions extends ToweletteStateExtensions
{
	int towelette_getLuminance();
	
	Block towelette_getBlock();
	
	boolean towelette_isSideSolidFullSquare(BlockView world, BlockPos pos, Direction direction);
}
