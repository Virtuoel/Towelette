package virtuoel.towelette.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public interface Fluidloggable extends Waterloggable
{
	public static final FluidProperty FLUID = FluidProperty.FLUID;
	
	@Override
	default boolean canFillWithFluid(BlockView var1, BlockPos var2, BlockState var3, Fluid var4)
	{
		return FLUID.getFluidState(var3).isEmpty() && var4.getDefaultState().isStill() && FLUID.isValid(var4);
	}
	
	@Override
	default boolean tryFillWithFluid(IWorld var1, BlockPos var2, BlockState var3, FluidState var4)
	{
		return fillImpl(var1, var2, var3, var4);
	}
	
	@Override
	default Fluid tryDrainFluid(IWorld var1, BlockPos var2, BlockState var3)
	{
		FluidState fluidState = FLUID.getFluidState(var3);
		if(!fluidState.isEmpty())
		{
			var3 = var3.with(FLUID, FLUID.of(Fluids.EMPTY));
			if(var3.contains(Properties.WATERLOGGED))
			{
				var3 = var3.with(Properties.WATERLOGGED, false);
			}
			var1.setBlockState(var2, var3, 3);
		}
		return fluidState.getFluid();
	}
	
	public static boolean fillImpl(IWorld var1, BlockPos var2, BlockState var3, FluidState var4)
	{
		if(var4.isStill() && FLUID.getFluidState(var3).isEmpty() && FLUID.isValid(var4))
		{
			if(!var1.isClient())
			{
				final Fluid fluid = var4.getFluid();
				if(fluid == Fluids.WATER && var3.contains(Properties.WATERLOGGED))
				{
					var3 = var3.with(Properties.WATERLOGGED, true);
				}
				var1.setBlockState(var2, var3.with(FLUID, FLUID.of(var4)), 3);
				var1.getFluidTickScheduler().schedule(var2, fluid, fluid.method_15789(var1));
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
}
