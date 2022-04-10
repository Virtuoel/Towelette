package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.TagCompatibility;

@Mixin(FireBlock.class)
public class FireBlockMixin
{
	@Inject(at = @At("HEAD"), method = { "getSpreadChance", "getBurnChance" }, cancellable = true)
	private void onGetChances(BlockState state, CallbackInfoReturnable<Integer> info)
	{
		if (TagCompatibility.isIn(FluidUtils.getFluid(state), TagCompatibility.FluidTags.WATER))
		{
			info.setReturnValue(0);
		}
	}
}
