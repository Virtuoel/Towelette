package virtuoel.towelette.util;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.ToweletteConfig;

public class FluidUtils
{
	public static Identifier getFluidId(ItemPlacementContext context)
	{
		return getFluidId(context.getWorld().getFluidState(context.getBlockPos()));
	}
	
	public static Identifier getFluidId(FluidState fluid)
	{
		return getFluidId(fluid.getFluid());
	}
	
	public static Identifier getFluidId(Fluid fluid)
	{
		final Identifier id = Registry.FLUID.getId(fluid);
		return isValid(id) ? id : Registry.FLUID.getDefaultId();
	}
	
	public static boolean isValid(FluidState fluid)
	{
		return isValid(fluid.getFluid());
	}
	
	public static boolean isValid(Fluid fluid)
	{
		return isValid(Registry.FLUID.getId(fluid));
	}
	
	public static boolean isValid(Identifier id)
	{
		return FluidProperties.FLUID.getValues().contains(id);
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, ItemUsageContext context)
	{
		return getStateWithFluid(state, context.getWorld(), context.getBlockPos());
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, BlockView world, BlockPos pos)
	{
		if(state != null && (state.contains(Properties.WATERLOGGED) || state.contains(FluidProperties.FLUID)))
		{
			return getStateWithFluid(state, world.getFluidState(pos));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState blockState, FluidState fluidState)
	{
		if(blockState != null)
		{
			final boolean isDoubleSlab = blockState.contains(Properties.SLAB_TYPE) && blockState.get(Properties.SLAB_TYPE) == SlabType.DOUBLE;
			
			if(blockState.contains(Properties.WATERLOGGED))
			{
				blockState = blockState.with(Properties.WATERLOGGED, !isDoubleSlab && fluidState.getFluid() == Fluids.WATER);
			}
			
			if(blockState.contains(FluidProperties.FLUID))
			{
				blockState = blockState.with(FluidProperties.FLUID, getFluidId(isDoubleSlab ? Fluids.EMPTY : fluidState.getFluid()));
			}
			
			if(blockState.contains(FluidProperties.FALLING))
			{
				blockState = blockState.with(FluidProperties.FALLING, fluidState.getEntries().containsKey(Properties.FALLING) && fluidState.get(Properties.FALLING));
			}
			
			if(blockState.contains(FluidProperties.LEVEL_1_8))
			{
				blockState = blockState.with(FluidProperties.LEVEL_1_8, fluidState.getEntries().containsKey(Properties.LEVEL_1_8) ? fluidState.get(Properties.LEVEL_1_8) : 8);
			}
		}
		return blockState;
	}
	
	public static FluidState getFluidState(BlockState blockState)
	{
		FluidState state = getFluidState(getFluid(blockState));
		
		if(blockState.contains(FluidProperties.FALLING) && state.getEntries().containsKey(Properties.FALLING))
		{
			state = state.with(Properties.FALLING, blockState.get(FluidProperties.FALLING));
		}
		
		if(blockState.contains(FluidProperties.LEVEL_1_8) && state.getEntries().containsKey(Properties.LEVEL_1_8))
		{
			state = state.with(Properties.LEVEL_1_8, blockState.get(FluidProperties.LEVEL_1_8));
		}
		
		return state;
	}
	
	public static FluidState getFluidState(Fluid fluid)
	{
		final FluidState state = fluid.getDefaultState();
		return state.getEntries().containsKey(Properties.FALLING) ? state.with(Properties.FALLING, false) : state;
	}
	
	public static Fluid getFluid(BlockState state)
	{
		Fluid ret = Fluids.EMPTY;
		if(state != null)
		{
			if(state.contains(FluidProperties.FLUID))
			{
				ret = Registry.FLUID.get(state.get(FluidProperties.FLUID));
			}
			
			if(ret.getDefaultState().isEmpty() && state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED))
			{
				ret = Fluids.WATER;
			}
		}
		return ret;
	}
	
	public static boolean scheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		return scheduleFluidTick(getFluid(state), world, pos);
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
	
	public static boolean canFillWithFluid(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1, Fluid fluid_1)
	{
		if((fluid_1 == Fluids.WATER && blockState_1.contains(Properties.WATERLOGGED) && !blockState_1.get(Properties.WATERLOGGED)) || isValid(fluid_1))
		{
			final FluidState fluidState = getFluidState(blockState_1);
			
			if(fluidState.isEmpty() || !fluidState.isStill())
			{
				return true;
			}
			
			return Optional.ofNullable(ToweletteConfig.DATA.get("replaceableFluids"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(false);
		}
		
		return false;
	}
	
	public static boolean tryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState, FluidState fluidState)
	{
		if(canFillWithFluid(iWorld_1, blockPos_1, blockState, fluidState.getFluid()))
		{
			if(!iWorld_1.isClient())
			{
				final Fluid fluid = fluidState.getFluid();
				
				boolean filled = false;
				
				if(fluid == Fluids.WATER && blockState.contains(Properties.WATERLOGGED))
				{
					blockState = blockState.with(Properties.WATERLOGGED, true);
					filled = true;
				}
				
				if(blockState.contains(FluidProperties.FLUID))
				{
					blockState = blockState.with(FluidProperties.FLUID, getFluidId(fluidState));
					filled = true;
				}
				
				if(filled && blockState.contains(FluidProperties.FALLING))
				{
					blockState = blockState.with(FluidProperties.FALLING, fluidState.getEntries().containsKey(Properties.FALLING) && fluidState.get(Properties.FALLING));
				}
				
				if(filled && blockState.contains(FluidProperties.LEVEL_1_8))
				{
					blockState = blockState.with(FluidProperties.LEVEL_1_8, fluidState.getEntries().containsKey(Properties.LEVEL_1_8) ? fluidState.get(Properties.LEVEL_1_8) : 8);
				}
				
				if(filled)
				{
					iWorld_1.setBlockState(blockPos_1, blockState, 3);
				}
				
				iWorld_1.getFluidTickScheduler().schedule(blockPos_1, fluid, fluid.getTickRate(iWorld_1));
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static Fluid tryDrainFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1)
	{
		final FluidState fluidState = getFluidState(blockState_1);
		if(!fluidState.isEmpty())
		{
			if(blockState_1.contains(FluidProperties.FLUID))
			{
				blockState_1 = blockState_1.with(FluidProperties.FLUID, Registry.FLUID.getDefaultId());
			}
			
			if(blockState_1.contains(FluidProperties.FALLING))
			{
				blockState_1 = blockState_1.with(FluidProperties.FALLING, false);
			}
			
			if(blockState_1.contains(FluidProperties.LEVEL_1_8))
			{
				blockState_1 = blockState_1.with(FluidProperties.LEVEL_1_8, 8);
			}
			
			if(blockState_1.contains(Properties.WATERLOGGED))
			{
				blockState_1 = blockState_1.with(Properties.WATERLOGGED, false);
			}
			
			iWorld_1.setBlockState(blockPos_1, blockState_1, 3);
		}
		return fluidState.isStill() ? fluidState.getFluid() : Fluids.EMPTY;
	}
}
