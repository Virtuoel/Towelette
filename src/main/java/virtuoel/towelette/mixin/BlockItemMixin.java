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
	@Redirect(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;"))
	public BlockState getPlacementStateGetPlacementStateProxy(Block obj, ItemPlacementContext context)
	{
		return FluidUtils.getStateWithFluid(obj.getPlacementState(context), context);
	}
}
