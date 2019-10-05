package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin
{
	@ModifyArg(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z"))
	private BlockState placePlaceProxy(ItemPlacementContext context, BlockState state)
	{
		return FluidUtils.getStateWithFluid(state, context);
	}
}
