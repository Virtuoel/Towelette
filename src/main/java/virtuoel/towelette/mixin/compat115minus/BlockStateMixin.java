package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(BlockState.class)
public abstract class BlockStateMixin implements ToweletteBlockStateExtensions
{
	@Shadow(remap = false)
	abstract Block method_11614();
	@Shadow(remap = false)
	abstract FluidState method_11618();
	@Shadow(remap = false)
	abstract int method_11630();
	@Shadow(remap = false)
	abstract boolean method_20827(BlockView world, BlockPos pos, Direction direction);
	
	@Override
	public Block towelette_getBlock()
	{
		return method_11614();
	}
	
	@Override
	public int towelette_getLuminance()
	{
		return method_11630();
	}
	
	@Override
	public boolean towelette_isSideSolidFullSquare(BlockView world, BlockPos pos, Direction direction)
	{
		return method_20827(world, pos, direction);
	}
	
	@Inject(at = @At("RETURN"), method = "method_11630", cancellable = true, remap = false)
	private void onGetLuminance(CallbackInfoReturnable<Integer> info)
	{
		final ToweletteFluidStateExtensions fluidState = (ToweletteFluidStateExtensions) (Object)method_11618();
		
		if (fluidState.towelette_getFluid() != Fluids.EMPTY)
		{
			final BlockState fluidBlockState = fluidState.towelette_getBlockState();
			
			if (fluidBlockState != (BlockState) (Object) this)
			{
				info.setReturnValue(Math.max(info.getReturnValue(), ((ToweletteBlockStateExtensions) fluidBlockState).towelette_getLuminance()));
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "method_11578", remap = false)
	private void onGetStateForNeighborUpdate(Direction direction, BlockState blockState, WorldAccess world, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, pos);
	}
	
	@Inject(at = @At("RETURN"), method = "method_11580", remap = false)
	private void onOnBlockAdded(World world, BlockPos blockPos, BlockState blockState, boolean flag, CallbackInfo info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, blockPos);
	}
}
