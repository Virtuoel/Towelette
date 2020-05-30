package virtuoel.towelette.util;

import net.minecraft.block.Block;
import net.minecraft.block.TallSeagrassBlock;

public interface AutomaticFluidloggableMarker
{
	public static boolean shouldAddProperties(Block block)
	{
		return block instanceof AutomaticFluidloggableMarker && ((AutomaticFluidloggableMarker) block).shouldAddProperties();
	}
	
	default boolean shouldAddProperties()
	{
		return !(this instanceof TallSeagrassBlock);
	}
}
