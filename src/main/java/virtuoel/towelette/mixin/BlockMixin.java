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
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WebBlock;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;

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
	
	// TODO fix this
	@Inject(at = @At("RETURN"), method = "getLuminance", cancellable = true)
	public void getLuminance(BlockState state, CallbackInfoReturnable<Integer> info)
	{
		if(getDefaultState() != null)
		{
			FluidState fluidState = state.getFluidState();
			if(fluidState.getFluid() != Fluids.EMPTY)
			{
				int luminance = info.getReturnValue();
				BlockState fluidBlock = fluidState.getBlockState();
				if(fluidBlock == state)
				{
					info.setReturnValue(luminance);
				}
				info.setReturnValue(Math.max(luminance, fluidBlock.getLuminance()));
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
	public void getStateForNeighborUpdate(BlockState state, Direction dir, BlockState var3, IWorld world, BlockPos var5, BlockPos var6, CallbackInfoReturnable<BlockState> info)
	{
		Fluid fluid = FluidProperty.FLUID.unwrap(state);
		if(fluid != Fluids.EMPTY)
		{
			world.getFluidTickScheduler().schedule(var5, fluid, fluid.method_15789(world));
		}
	}
	
	@Overwrite
	public FluidState getFluidState(BlockState state)
	{
		Fluid fluid = FluidProperty.FLUID.unwrap(state);
		if(fluid instanceof BaseFluid)
		{
			return ((BaseFluid) fluid).getState(false);
		}
		return Fluids.EMPTY.getDefaultState();
	}
	
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		BlockState state = info.getReturnValue();
		if(state.contains(Properties.WATERLOGGED))
		{
			state = state.with(Properties.WATERLOGGED, false);
		}
		
		if(state.contains(FluidProperty.FLUID))
		{
			FluidState fluid = context.getWorld().getFluidState(context.getPos());
			info.setReturnValue(state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluid)));
		}
	}
	
	@Inject(at = @At("RETURN"), method = "appendProperties", cancellable = true)
	private void onAppendProperties(StateFactory.Builder<Block, BlockState> var1, CallbackInfo info)
	{
		// TODO surely a better way to do this
		Object self = (Object) this;
		if(
			self instanceof TorchBlock ||
			self instanceof PlantBlock ||
			self instanceof WebBlock ||
			self instanceof DragonEggBlock ||
		false)
		{
			FluidProperty.FLUID.tryAppendPropertySafely(var1);
		}
	}
}
