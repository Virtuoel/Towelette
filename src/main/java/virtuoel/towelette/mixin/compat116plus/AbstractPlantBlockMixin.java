package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractPlantBlock;
import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AbstractPlantBlock.class)
public abstract class AbstractPlantBlockMixin extends AbstractPlantPartBlock
{
	private AbstractPlantBlockMixin()
	{
		super(null, null, null, false);
	}
	
	@Inject(method = "getStateForNeighborUpdate", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
	private void onGetStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> info)
	{
		if (!this.tickWater)
		{
			info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), world, pos));
		}
	}
}
