package virtuoel.towelette.util;

import net.minecraft.block.Block;
import net.minecraft.block.TallSeagrassBlock;

public interface AutomaticWaterloggableMarker
{
	public static boolean shouldAddProperties(Block block)
	{
		return block instanceof AutomaticWaterloggableMarker && ((AutomaticWaterloggableMarker) block).towelette_shouldAddWaterloggableProperty();
	}
	
	default boolean towelette_shouldAddWaterloggableProperty()
	{
		return !(this instanceof TallSeagrassBlock);
	}
}
