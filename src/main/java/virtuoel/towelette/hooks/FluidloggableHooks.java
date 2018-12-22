package virtuoel.towelette.hooks;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;

public class FluidloggableHooks
{
	public static void hookGetPlacementState(Block self, ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		BlockState state = info.getReturnValue();
		if(state != null)
		{
			if(state.contains(FluidProperty.FLUID))
			{
				FluidState fluid = context.getWorld().getFluidState(context.getPos());
				if(state.contains(Properties.WATERLOGGED) && fluid.getFluid() != Fluids.WATER)
				{
					state = state.with(Properties.WATERLOGGED, false);
				}
				state = state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluid));
			}
			
			info.setReturnValue(state);
		}
	}
	
	public static void hookOnAppendProperties(Block self, StateFactory.Builder<Block, BlockState> var1, CallbackInfo info)
	{
		if(self instanceof Fluidloggable)
		{
			FluidProperty.FLUID.tryAppendPropertySafely(var1);
		}
	}
	
	public static void hookScheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		Fluid fluid = FluidProperty.FLUID.getFluid(state);
		if(fluid != Fluids.EMPTY)
		{
			world.getFluidTickScheduler().schedule(pos, fluid, fluid.method_15789(world));
		}
	}
}
