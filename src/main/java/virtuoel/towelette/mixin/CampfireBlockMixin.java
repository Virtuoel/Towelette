package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import virtuoel.towelette.util.TagCompatibility;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		if (TagCompatibility.isIn(context.getWorld().getFluidState(context.getBlockPos()), TagCompatibility.FluidTags.WATER))
		{
			info.setReturnValue(((ToweletteBlockStateExtensions) info.getReturnValue()).towelette_with(Properties.LIT, false).towelette_cast());
		}
	}
}
