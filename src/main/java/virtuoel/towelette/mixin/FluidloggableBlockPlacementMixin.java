package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.block.BlockItem;
import net.minecraft.item.block.WallStandingBlockItem;
import virtuoel.towelette.util.FluidUtils;

@Mixin({
	BlockItem.class,
	WallStandingBlockItem.class,
})
public abstract class FluidloggableBlockPlacementMixin
{
	@Redirect(method = "getBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;"))
	public BlockState getBlockStateGetPlacementStateProxy(Block obj, ItemPlacementContext context)
	{
		return FluidUtils.getStateWithFluid(obj.getPlacementState(context), context);
	}
}
