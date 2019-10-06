package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.BambooBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

@Mixin(BambooBlock.class)
public class BambooBlockMixin
{
	@ModifyVariable(method = "getPlacementState", at = @At(value = "LOAD", ordinal = 0), allow = 1, require = 1)
	private FluidState getPlacementStateFluidStateProxy(FluidState state)
	{
		return Fluids.EMPTY.getDefaultState();
	}
}
