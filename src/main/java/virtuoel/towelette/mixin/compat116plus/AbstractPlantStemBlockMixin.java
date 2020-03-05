package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.class_4863;
import net.minecraft.class_4865;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.util.FluidUtils;

@Mixin(class_4865.class)
public abstract class AbstractPlantStemBlockMixin extends class_4863
{
	private AbstractPlantStemBlockMixin(Settings settings, Direction direction, boolean bl)
	{
		super(settings, direction, bl);
	}
	
	@Inject(method = "getStateForNeighborUpdate", at = @At("RETURN"), cancellable = true)
	private void onGetStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> info)
	{
		if (!this.field_22508 && facing == this.field_22507 && neighborState.getBlock() == (Object) this)
		{
			info.setReturnValue(FluidUtils.getStateWithFluid(info.getReturnValue(), world, pos));
		}
	}
}
