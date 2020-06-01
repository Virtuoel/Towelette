package virtuoel.towelette.util;

import net.minecraft.block.Block;
import net.minecraft.block.TallSeagrassBlock;

public interface AutomaticFluidloggableMarker
{
	public static boolean shouldAddProperties(Block block)
	{
		return block instanceof AutomaticFluidloggableMarker && ((AutomaticFluidloggableMarker) block).towelette_shouldAddFluidloggableProperties();
	}
	
	default boolean towelette_shouldAddFluidloggableProperties()
	{
		return !(this instanceof TallSeagrassBlock);
	}
}
