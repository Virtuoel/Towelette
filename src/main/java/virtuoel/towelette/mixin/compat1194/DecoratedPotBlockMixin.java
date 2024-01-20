package virtuoel.towelette.mixin.compat1194;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.BlockState;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin
{
	@ModifyVariable(method = "onStateReplaced", at = @At(value = "STORE", ordinal = 0))
	private BlockEntity towelette$onStateReplaced$getBlockEntity(BlockEntity blockEntity, BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		return state.getBlock() == newState.getBlock() ? null : blockEntity;
	}
}
