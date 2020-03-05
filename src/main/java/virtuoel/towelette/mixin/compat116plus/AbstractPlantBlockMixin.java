package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.class_4863;
import net.minecraft.class_4864;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

@Mixin(class_4864.class)
public abstract class AbstractPlantBlockMixin extends class_4863
{
	private AbstractPlantBlockMixin(Settings settings, Direction direction, boolean bl)
	{
		super(settings, direction, bl);
	}

	@Inject(method = "getStateForNeighborUpdate", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
	private void onGetStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> info)
	{
		if (!this.field_22508)
		{
			info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), world, pos));
		}
	}
}
