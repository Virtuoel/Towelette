package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.TallBlockItem;

@Mixin(TallBlockItem.class)
public abstract class TallBlockItemMixin
{
	@Inject(at = @At("HEAD"), method = "place", cancellable = true)
	private void onPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> info)
	{
		info.setReturnValue(context.getWorld().setBlockState(context.getBlockPos(), state, 26));
	}
}
