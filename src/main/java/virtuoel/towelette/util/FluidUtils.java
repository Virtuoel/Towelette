package virtuoel.towelette.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.math.DoubleMath;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.SlicedVoxelShape;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import virtuoel.statement.util.VersionUtils;
import virtuoel.towelette.api.FluidBlockingShapeProvider;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.mixin.VoxelShapeAccessor;

public class FluidUtils
{
	public static boolean isFluidFlowBlocked(Direction direction, BlockView world, VoxelShape shape, BlockState blockState, BlockPos blockPos, VoxelShape fromShape, BlockState fromState, BlockPos fromPos)
	{
		final Block block = ((ToweletteBlockStateExtensions) blockState).towelette_getBlock();
		if (block instanceof FluidBlockingShapeProvider)
		{
			shape = ((FluidBlockingShapeProvider) block).getFluidBlockingShape(blockState, world, blockPos);
		}
		
		final Block fromBlock = ((ToweletteBlockStateExtensions) fromState).towelette_getBlock();
		if (fromBlock instanceof FluidBlockingShapeProvider)
		{
			fromShape = ((FluidBlockingShapeProvider) fromBlock).getFluidBlockingShape(fromState, world, fromPos);
		}
		
		if (direction.getAxis() != Direction.Axis.Y)
		{
			final boolean accurateFlowBlocking = ToweletteConfig.COMMON.accurateFlowBlocking.get();
			
			if (accurateFlowBlocking)
			{
				if (shape != VoxelShapes.fullCube() && fromShape != VoxelShapes.fullCube())
				{
					final ToweletteFluidStateExtensions fluidState = (ToweletteFluidStateExtensions) (Object) world.getFluidState(fromPos);
					final VoxelShape inverseShape = fluidState.towelette_isEmpty() ? VoxelShapes.empty() : VoxelShapes.combine(VoxelShapes.fullCube(), fluidState.towelette_getShape(world, fromPos), BooleanBiFunction.ONLY_FIRST);
					final VoxelShape combinedFromShape = VoxelShapes.combine(fromShape, inverseShape, BooleanBiFunction.OR);
					
					final Direction.Axis axis = direction.getAxis();
					final boolean positiveDirection = direction.getDirection() == Direction.AxisDirection.POSITIVE;
					VoxelShape positiveShape = positiveDirection ? shape : combinedFromShape;
					VoxelShape negativeShape = positiveDirection ? combinedFromShape : shape;
					
					if (positiveShape != VoxelShapes.empty() && !DoubleMath.fuzzyEquals(positiveShape.getMax(axis), 1.0D, 1.0E-7D))
					{
						positiveShape = VoxelShapes.empty();
					}
					
					if (negativeShape != VoxelShapes.empty() && !DoubleMath.fuzzyEquals(negativeShape.getMin(axis), 0.0D, 1.0E-7D))
					{
						negativeShape = VoxelShapes.empty();
					}
					
					return !VoxelShapes.matchesAnywhere(VoxelShapes.fullCube(), VoxelShapes.combine(new SlicedVoxelShape(positiveShape, axis, ((VoxelShapeAccessor) positiveShape).towelette_getVoxels().getSize(axis) - 1), new SlicedVoxelShape(negativeShape, axis, 0), BooleanBiFunction.OR), BooleanBiFunction.ONLY_FIRST);
				}
				else
				{
					return true;
				}
			}
		}
		
		return VoxelShapes.adjacentSidesCoverSquare(shape, fromShape, direction);
	}
	
	public static Identifier getFluidId(Fluid fluid)
	{
		final Identifier id = Registry.FLUID.getId(fluid);
		return propertyContains(id) ? id : Registry.FLUID.getDefaultId();
	}
	
	public static boolean propertyContains(Fluid fluid)
	{
		return propertyContains(Registry.FLUID.getId(fluid));
	}
	
	public static boolean propertyContains(Identifier id)
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
		final ImmutableMap<?, Comparable<?>> blockEntries = ((ToweletteBlockStateExtensions) state).towelette_getEntries();
		
		if (state != null && (blockEntries.containsKey(Properties.WATERLOGGED) || blockEntries.containsKey(FluidProperties.FLUID)))
		{
			return getStateWithFluid(state, world.getFluidState(pos));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState block, FluidState fluid)
	{
		if (block != null)
		{
			final ToweletteFluidStateExtensions fluidState = (ToweletteFluidStateExtensions) (Object) fluid;
			final ImmutableMap<?, Comparable<?>> fluidEntries = fluidState.towelette_getEntries();
			ToweletteStateExtensions blockState = ((ToweletteBlockStateExtensions) block);
			final ImmutableMap<?, Comparable<?>> blockEntries = blockState.towelette_getEntries();
			
			final boolean isDoubleSlab = blockEntries.containsKey(Properties.SLAB_TYPE) && blockState.<SlabType>towelette_get(Properties.SLAB_TYPE) == SlabType.DOUBLE;
			
			if (blockEntries.containsKey(Properties.WATERLOGGED))
			{
				blockState = blockState.towelette_with(Properties.WATERLOGGED, !isDoubleSlab && fluidState.towelette_getFluid() == Fluids.WATER);
			}
			
			final boolean hasFluidLevel = blockEntries.containsKey(FluidProperties.LEVEL_1_8);
			final boolean cannotFillWithFluid = isDoubleSlab || (!hasFluidLevel && !fluidState.towelette_isEmpty() && !fluidState.towelette_isStill());
			
			if (blockEntries.containsKey(FluidProperties.FLUID))
			{
				blockState = blockState.towelette_with(FluidProperties.FLUID, cannotFillWithFluid ? Registry.FLUID.getDefaultId() : getFluidId(fluidState.towelette_getFluid()));
			}
			
			if (blockEntries.containsKey(FluidProperties.FALLING))
			{
				blockState = blockState.towelette_with(FluidProperties.FALLING, fluidEntries.containsKey(Properties.FALLING) && fluidState.<Boolean>towelette_get(Properties.FALLING));
			}
			
			if (hasFluidLevel)
			{
				blockState = blockState.towelette_with(FluidProperties.LEVEL_1_8, !isDoubleSlab && fluidEntries.containsKey(Properties.LEVEL_1_8) ? fluidState.towelette_get(Properties.LEVEL_1_8) : 8);
			}
			
			return blockState.towelette_cast();
		}
		
		return null;
	}
	
	public static FluidState getFluidState(BlockState block)
	{
		FluidState fluid = getFluid(block).getDefaultState();
		
		ToweletteStateExtensions fluidState = (ToweletteStateExtensions) (Object) fluid;
		final ImmutableMap<?, Comparable<?>> fluidEntries = fluidState.towelette_getEntries();
		ToweletteBlockStateExtensions blockState = ((ToweletteBlockStateExtensions) block);
		final ImmutableMap<?, Comparable<?>> blockEntries = blockState.towelette_getEntries();
		
		if (fluidEntries.containsKey(Properties.FALLING))
		{
			fluidState = fluidState.towelette_with(Properties.FALLING, blockEntries.containsKey(FluidProperties.FALLING) && blockState.<Boolean>towelette_get(FluidProperties.FALLING));
		}
		
		if (blockEntries.containsKey(FluidProperties.LEVEL_1_8) && fluidEntries.containsKey(Properties.LEVEL_1_8))
		{
			fluidState = fluidState.towelette_with(Properties.LEVEL_1_8, blockState.<Integer>towelette_get(FluidProperties.LEVEL_1_8));
		}
		
		return fluidState.<FluidState>towelette_cast();
	}
	
	public static Fluid getFluid(BlockState state)
	{
		Fluid ret = Fluids.EMPTY;
		
		if (state != null)
		{
			ToweletteBlockStateExtensions blockState = ((ToweletteBlockStateExtensions) state);
			final ImmutableMap<?, Comparable<?>> blockEntries = blockState.towelette_getEntries();
			
			if (blockEntries.containsKey(FluidProperties.FLUID))
			{
				ret = Registry.FLUID.get(blockState.<Identifier>towelette_get(FluidProperties.FLUID));
			}
			
			if (((ToweletteFluidStateExtensions) (Object) ret.getDefaultState()).towelette_isEmpty() && blockEntries.containsKey(Properties.WATERLOGGED) && blockState.<Boolean>towelette_get(Properties.WATERLOGGED))
			{
				ret = Fluids.WATER;
			}
		}
		
		return ret;
	}
	
	public static boolean scheduleFluidTick(BlockState state, WorldAccess world, BlockPos pos)
	{
		return scheduleFluidTick(getFluid(state), world, pos);
	}
	
	public static boolean scheduleFluidTick(FluidState fluidState, WorldAccess world, BlockPos pos)
	{
		ToweletteFluidStateExtensions state = (ToweletteFluidStateExtensions) (Object) fluidState;
		if (!state.towelette_isEmpty())
		{
			scheduleFluidTickImpl(state.towelette_getFluid(), world, pos);
			return true;
		}
		
		return false;
	}
	
	public static boolean scheduleFluidTick(Fluid fluid, WorldAccess world, BlockPos pos)
	{
		if (!((ToweletteFluidStateExtensions) (Object) fluid.getDefaultState()).towelette_isEmpty())
		{
			scheduleFluidTickImpl(fluid, world, pos);
			return true;
		}
		
		return false;
	}
	
	private static void scheduleFluidTickImpl(Fluid fluid, WorldAccess world, BlockPos pos)
	{
		final int rate = ((ToweletteFluidExtensions) fluid).towelette_getTickRate(world);
		((ToweletteWorldAccessExtensions) world).towelette_scheduleFluidTick(pos, fluid, rate);
	}
	
	public static final Optional<Function<Object, Object>> FLUID_TICK_SCHEDULER_GETTER;
	
	static
	{
		if (VersionUtils.MINOR <= 17)
		{
			Method m = null;
			try
			{
				final String getFluidTickScheduler = FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_1936", "method_8405", "()Lnet/minecraft/class_1951;");
				m = WorldAccess.class.getMethod(getFluidTickScheduler);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				
			}
			
			final Method method = m;
			final Function<Object, Object> getter = w ->
			{
				try
				{
					return method.invoke(w);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					return null;
				}
			};
			
			FLUID_TICK_SCHEDULER_GETTER = method == null ? Optional.empty() : Optional.of(getter);
		}
		else
		{
			FLUID_TICK_SCHEDULER_GETTER = Optional.empty();
		}
	}
	
	private static boolean isFluidValidForState(BlockState state, Fluid fluid)
	{
		ToweletteBlockStateExtensions blockState = ((ToweletteBlockStateExtensions) state);
		final ImmutableMap<?, Comparable<?>> blockEntries = blockState.towelette_getEntries();
		
		if (fluid == Fluids.WATER && blockEntries.containsKey(Properties.WATERLOGGED) && !blockState.<Boolean>towelette_get(Properties.WATERLOGGED))
		{
			return true;
		}
		
		if (blockEntries.containsKey(FluidProperties.FLUID) && propertyContains(fluid))
		{
			if (!fluid.isStill(fluid.getDefaultState()) && !blockEntries.containsKey(FluidProperties.LEVEL_1_8))
			{
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public static boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		if (isFluidValidForState(state, fluid))
		{
			final ToweletteFluidStateExtensions fluidState = (ToweletteFluidStateExtensions) (Object) getFluidState(state);
			
			if (fluidState.towelette_isEmpty() || !fluidState.towelette_isStill())
			{
				return true;
			}
			
			return ToweletteConfig.COMMON.replaceableFluids.get();
		}
		
		return false;
	}
	
	public static boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		final Fluid fluid = ((ToweletteFluidStateExtensions) (Object) fluidState).towelette_getFluid();
		
		if (canFillWithFluid(world, pos, blockState, fluid))
		{
			if (!world.isClient())
			{
				final BlockState filled = getStateWithFluid(blockState, fluidState);
				
				if (filled != blockState)
				{
					world.setBlockState(pos, filled, 3);
				}
				
				scheduleFluidTickImpl(fluid, world, pos);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static Fluid tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state)
	{
		final ToweletteFluidStateExtensions fluidState = (ToweletteFluidStateExtensions) (Object) getFluidState(state);
		
		if (!fluidState.towelette_isEmpty())
		{
			ToweletteStateExtensions blockState = ((ToweletteStateExtensions) state);
			final ImmutableMap<?, Comparable<?>> blockEntries = blockState.towelette_getEntries();
			
			if (blockEntries.containsKey(FluidProperties.FLUID))
			{
				blockState = blockState.towelette_with(FluidProperties.FLUID, Registry.FLUID.getDefaultId());
			}
			
			if (blockEntries.containsKey(FluidProperties.FALLING))
			{
				blockState = blockState.towelette_with(FluidProperties.FALLING, false);
			}
			
			if (blockEntries.containsKey(FluidProperties.LEVEL_1_8))
			{
				blockState = blockState.towelette_with(FluidProperties.LEVEL_1_8, 8);
			}
			
			if (blockEntries.containsKey(Properties.WATERLOGGED))
			{
				blockState = blockState.towelette_with(Properties.WATERLOGGED, false);
			}
			
			world.setBlockState(pos, blockState.towelette_cast(), 3);
		}
		
		return fluidState.towelette_isStill() ? fluidState.towelette_getFluid() : Fluids.EMPTY;
	}
}
