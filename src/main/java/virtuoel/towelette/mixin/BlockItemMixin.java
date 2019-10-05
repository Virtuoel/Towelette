package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin
{
	@Shadow
	public final Block block; //This is deprecated however it's the most convenience
	
	@SoftOverride
	public BlockState getPlacementStateGetPlacementStateProxy(ItemPlacementContext context)
	{
		return FluidUtils.getStateWithFluid(block.getPlacementState(context), context);
	}
}
