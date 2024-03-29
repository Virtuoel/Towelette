package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;setDefaultState(Lnet/minecraft/block/BlockState;)V", remap = true), remap = false)
	private BlockState onConstructSetDefaultState(BlockState state)
	{
		return FluidUtils.getStateWithFluid(state, Fluids.EMPTY.getDefaultState());
	}
}
