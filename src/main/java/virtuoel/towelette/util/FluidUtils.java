package virtuoel.towelette.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperty;

public class FluidUtils
{
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, ItemUsageContext context)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, context.getWorld().getFluidState(context.getBlockPos()));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, BlockView world, BlockPos pos)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, world.getFluidState(pos));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, Fluid fluid)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, fluid.getDefaultState());
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, FluidState fluid)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, fluid);
		}
		
		return state;
	}
	
	private static BlockState getStateWithFluidImpl(@Nonnull BlockState state, FluidState fluid)
	{
		final boolean isDoubleSlab = state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE;
		if(state.contains(Properties.WATERLOGGED))
		{
			state = state.with(Properties.WATERLOGGED, !isDoubleSlab && fluid.getFluid() == Fluids.WATER);
		}
		return state.with(FluidProperty.FLUID, isDoubleSlab ? FluidProperty.FLUID.of(Fluids.EMPTY) : FluidProperty.FLUID.of(fluid));
	}
	
	public static boolean scheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		return scheduleFluidTick(FluidProperty.FLUID.getFluid(state), world, pos);
	}
	
	public static boolean scheduleFluidTick(FluidState state, IWorld world, BlockPos pos)
	{
		if(!state.isEmpty())
		{
			scheduleFluidTickImpl(state.getFluid(), world, pos);
			return true;
		}
		return false;
	}
	
	public static boolean scheduleFluidTick(Fluid fluid, IWorld world, BlockPos pos)
	{
		if(!fluid.getDefaultState().isEmpty())
		{
			scheduleFluidTickImpl(fluid, world, pos);
			return true;
		}
		return false;
	}
	
	private static void scheduleFluidTickImpl(Fluid fluid, IWorld world, BlockPos pos)
	{
		world.getFluidTickScheduler().schedule(pos, fluid, fluid.getTickRate(world));
	}
}
