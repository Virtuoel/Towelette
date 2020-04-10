package virtuoel.towelette.util;

import net.minecraft.block.Block;
import net.minecraft.block.Material;

public interface ToweletteBlockStateExtensions
{
	int towelette_getLuminance();
	
	Block towelette_getBlock();
	
	Material towelette_getMaterial();
}
