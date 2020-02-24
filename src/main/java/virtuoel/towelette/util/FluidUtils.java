package virtuoel.towelette.util;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.math.DoubleMath;
import com.google.gson.JsonElement;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.SlicedVoxelShape;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.mixin.VoxelShapeAccessor;

public class FluidUtils
{
	public static boolean isFluidFlowBlocked(Direction direction, BlockView world, VoxelShape shape, BlockState blockState, BlockPos blockPos, VoxelShape otherShape, BlockState otherState, BlockPos otherPos)
	{
		if (direction.getAxis() != Direction.Axis.Y)
		{
			final boolean accurateFlowBlocking = Optional.ofNullable(ToweletteConfig.DATA.get("accurateFlowBlocking"))
				.filter(JsonElement::isJsonPrimitive)
				.map(JsonElement::getAsBoolean).orElse(true);
			
			if (accurateFlowBlocking)
			{
				if (shape != VoxelShapes.fullCube() && otherShape != VoxelShapes.fullCube())
				{
					final FluidState fluidState = world.getFluidState(otherPos);
					final VoxelShape inverseShape = fluidState.isEmpty() ? VoxelShapes.empty() : VoxelShapes.combine(VoxelShapes.fullCube(), fluidState.getShape(world, otherPos), BooleanBiFunction.ONLY_FIRST);
					final VoxelShape combinedOtherShape = VoxelShapes.combine(otherShape, inverseShape, BooleanBiFunction.OR);
					
					final Direction.Axis axis = direction.getAxis();
					final boolean positiveDirection = direction.getDirection() == Direction.AxisDirection.POSITIVE;
					VoxelShape fromShape = positiveDirection ? shape : combinedOtherShape;
					VoxelShape toShape = positiveDirection ? combinedOtherShape : shape;
					if (fromShape != VoxelShapes.empty() && !DoubleMath.fuzzyEquals(fromShape.getMaximum(axis), 1.0D, 1.0E-7D))
					{
						fromShape = VoxelShapes.empty();
					}
					
					if (toShape != VoxelShapes.empty() && !DoubleMath.fuzzyEquals(toShape.getMinimum(axis), 0.0D, 1.0E-7D))
					{
						toShape = VoxelShapes.empty();
					}
					
					return !VoxelShapes.matchesAnywhere(VoxelShapes.fullCube(), VoxelShapes.combine(new SlicedVoxelShape(fromShape, axis, ((VoxelShapeAccessor) fromShape).getVoxels().getSize(axis) - 1), new SlicedVoxelShape(toShape, axis, 0), BooleanBiFunction.OR), BooleanBiFunction.ONLY_FIRST);
				}
				else
				{
					return true;
				}
			}
		}
		
		return VoxelShapes.adjacentSidesCoverSquare(shape, otherShape, direction);
	}
	
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
		if (state != null && (state.contains(Properties.WATERLOGGED) || state.contains(FluidProperties.FLUID)))
		{
			return getStateWithFluid(state, world.getFluidState(pos));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState blockState, FluidState fluidState)
	{
		if (blockState != null)
		{
			final boolean isDoubleSlab = blockState.contains(Properties.SLAB_TYPE) && blockState.get(Properties.SLAB_TYPE) == SlabType.DOUBLE;
			
			if (blockState.contains(Properties.WATERLOGGED))
			{
				blockState = blockState.with(Properties.WATERLOGGED, !isDoubleSlab && fluidState.getFluid() == Fluids.WATER);
			}
			
			if (blockState.contains(FluidProperties.FLUID))
			{
				blockState = blockState.with(FluidProperties.FLUID, getFluidId(isDoubleSlab ? Fluids.EMPTY : fluidState.getFluid()));
			}
			
			if (blockState.contains(FluidProperties.FALLING))
			{
				blockState = blockState.with(FluidProperties.FALLING, fluidState.getEntries().containsKey(Properties.FALLING) && fluidState.get(Properties.FALLING));
			}
			
			if (blockState.contains(FluidProperties.LEVEL_1_8))
			{
				blockState = blockState.with(FluidProperties.LEVEL_1_8, fluidState.getEntries().containsKey(Properties.LEVEL_1_8) ? fluidState.get(Properties.LEVEL_1_8) : 8);
			}
		}
		return blockState;
	}
	
	public static FluidState getFluidState(BlockState blockState)
	{
		FluidState state = getFluid(blockState).getDefaultState();
		
		if (state.getEntries().containsKey(Properties.FALLING))
		{
			state = state.with(Properties.FALLING, blockState.contains(FluidProperties.FALLING) && blockState.get(FluidProperties.FALLING));
		}
		
		if (blockState.contains(FluidProperties.LEVEL_1_8) && state.getEntries().containsKey(Properties.LEVEL_1_8))
		{
			state = state.with(Properties.LEVEL_1_8, blockState.get(FluidProperties.LEVEL_1_8));
		}
		
		return state;
	}
	
	public static Fluid getFluid(BlockState state)
	{
		Fluid ret = Fluids.EMPTY;
		if (state != null)
		{
			if (state.contains(FluidProperties.FLUID))
			{
				ret = Registry.FLUID.get(state.get(FluidProperties.FLUID));
			}
			
			if (ret.getDefaultState().isEmpty() && state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED))
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
		if (!state.isEmpty())
		{
			scheduleFluidTickImpl(state.getFluid(), world, pos);
			return true;
		}
		return false;
	}
	
	public static boolean scheduleFluidTick(Fluid fluid, IWorld world, BlockPos pos)
	{
		if (!fluid.getDefaultState().isEmpty())
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
	
	public static boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		if ((fluid == Fluids.WATER && state.contains(Properties.WATERLOGGED) && !state.get(Properties.WATERLOGGED)) || isValid(fluid))
		{
			final FluidState fluidState = getFluidState(state);
			
			if (fluidState.isEmpty() || !fluidState.isStill())
			{
				return true;
			}
			
			return Optional.ofNullable(ToweletteConfig.DATA.get("replaceableFluids"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(false);
		}
		
		return false;
	}
	
	public static boolean tryFillWithFluid(IWorld world, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		if (canFillWithFluid(world, pos, blockState, fluidState.getFluid()))
		{
			if (!world.isClient())
			{
				final Fluid fluid = fluidState.getFluid();
				
				boolean filled = false;
				
				if (fluid == Fluids.WATER && blockState.contains(Properties.WATERLOGGED))
				{
					blockState = blockState.with(Properties.WATERLOGGED, true);
					filled = true;
				}
				
				if (blockState.contains(FluidProperties.FLUID))
				{
					blockState = blockState.with(FluidProperties.FLUID, getFluidId(fluidState));
					filled = true;
				}
				
				if (filled && blockState.contains(FluidProperties.FALLING))
				{
					blockState = blockState.with(FluidProperties.FALLING, fluidState.getEntries().containsKey(Properties.FALLING) && fluidState.get(Properties.FALLING));
				}
				
				if (filled && blockState.contains(FluidProperties.LEVEL_1_8))
				{
					blockState = blockState.with(FluidProperties.LEVEL_1_8, fluidState.getEntries().containsKey(Properties.LEVEL_1_8) ? fluidState.get(Properties.LEVEL_1_8) : 8);
				}
				
				if (filled)
				{
					world.setBlockState(pos, blockState, 3);
				}
				
				world.getFluidTickScheduler().schedule(pos, fluid, fluid.getTickRate(world));
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static Fluid tryDrainFluid(IWorld world, BlockPos pos, BlockState state)
	{
		final FluidState fluidState = getFluidState(state);
		if (!fluidState.isEmpty())
		{
			if (state.contains(FluidProperties.FLUID))
			{
				state = state.with(FluidProperties.FLUID, Registry.FLUID.getDefaultId());
			}
			
			if (state.contains(FluidProperties.FALLING))
			{
				state = state.with(FluidProperties.FALLING, false);
			}
			
			if (state.contains(FluidProperties.LEVEL_1_8))
			{
				state = state.with(FluidProperties.LEVEL_1_8, 8);
			}
			
			if (state.contains(Properties.WATERLOGGED))
			{
				state = state.with(Properties.WATERLOGGED, false);
			}
			
			world.setBlockState(pos, state, 3);
		}
		return fluidState.isStill() ? fluidState.getFluid() : Fluids.EMPTY;
	}
}
