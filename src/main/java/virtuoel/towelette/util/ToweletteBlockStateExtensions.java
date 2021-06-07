package virtuoel.towelette.util;

import net.minecraft.block.Block;
import net.minecraft.block.Material;

public interface ToweletteBlockStateExtensions extends ToweletteStateExtensions
{
	int towelette_getLuminance();
	
	Block towelette_getBlock();
	
	Material towelette_getMaterial();
}
