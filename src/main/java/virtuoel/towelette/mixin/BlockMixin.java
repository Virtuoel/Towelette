package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.hooks.FluidloggableHooks;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Shadow StateFactory<Block, BlockState> stateFactory;
	@Shadow abstract BlockState getDefaultState();
	@Shadow abstract void setDefaultState(BlockState state);
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void onConstruct(Block.Settings var1, CallbackInfo info)
	{
		BlockState defaultState = getDefaultState();
		if(defaultState.contains(FluidProperty.FLUID))
		{
			setDefaultState(defaultState.with(FluidProperty.FLUID, FluidProperty.FLUID.of(Fluids.EMPTY)));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState state, Direction dir, BlockState var3, IWorld world, BlockPos var5, BlockPos var6, CallbackInfoReturnable<BlockState> info)
	{
		Fluid fluid = FluidProperty.FLUID.getFluid(state);
		if(fluid != Fluids.EMPTY)
		{
			world.getFluidTickScheduler().schedule(var5, fluid, fluid.method_15789(world));
		}
	}
	
	@Overwrite
	public FluidState getFluidState(BlockState state)
	{
		return FluidProperty.FLUID.getFluidState(state);
	}
	
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookGetPlacementState((Block) (Object) this, context, info);
	}
	
	@Inject(at = @At("RETURN"), method = "appendProperties", cancellable = true)
	private void onAppendProperties(StateFactory.Builder<Block, BlockState> var1, CallbackInfo info)
	{
		FluidloggableHooks.hookOnAppendProperties((Block) (Object) this, var1, info);
	}
}
