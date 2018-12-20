package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.block.TallBlockItem;

@Mixin(TallBlockItem.class)
public abstract class TallBlockItemMixin
{
	@Inject(at = @At("HEAD"), method = "setBlockState", cancellable = true)
	public void onSetBlockState(ItemPlacementContext var1, BlockState var2, CallbackInfoReturnable<Boolean> info)
	{
		info.setReturnValue(var1.getWorld().setBlockState(var1.getPos(), var2, 26));
	}
}
