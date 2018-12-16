package virtuoel.towelette.fluid;

import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;

public abstract class ExtendedWaterFluid extends WaterFluid
{
	BlockState blockState;
	Function<Fluid, BlockState> blockStateSupplier;
	
	Fluid still;
	Function<Fluid, Fluid> stillSupplier;
	
	Fluid flowing;
	Function<Fluid, Fluid> flowingSupplier;
	
	Item bucket;
	Function<Fluid, Item> bucketSupplier;
	
	public ExtendedWaterFluid(Function<Fluid, Fluid> still, Function<Fluid, Fluid> flowing, Function<Fluid, BlockState> blockState, Function<Fluid, Item> bucket)
	{
		stillSupplier = still;
		flowingSupplier = flowing;
		blockStateSupplier = blockState;
		bucketSupplier = bucket;
	}
	
	@Override
	public Fluid getStill()
	{
		return still == null ? (still = stillSupplier.apply(this)) : still;
	}
	
	@Override
	public Fluid getFlowing()
	{
		return flowing == null ? (flowing = flowingSupplier.apply(this)) : flowing;
	}
	
	@Override
	public Item getBucketItem()
	{
		return bucket == null ? (bucket = bucketSupplier.apply(this)) : bucket;
	}
	
	@Override
	public boolean matchesType(Fluid var1)
	{
		return var1 == getStill() || var1 == getFlowing();
	}
	
	@Override
	public BlockState toBlockState(FluidState var1)
	{
		return (blockState == null ? (blockState = blockStateSupplier.apply(this)) : blockState).with(Properties.FLUID_BLOCK_LEVEL, method_15741(var1));
	}
	
	public static class Flowing extends ExtendedWaterFluid
	{
		public Flowing(Function<Fluid, Fluid> still, Function<Fluid, Fluid> flowing, Function<Fluid, BlockState> blockState, Function<Fluid, Item> bucket)
		{
			super(still, flowing, blockState, bucket);
		}
		
		@Override
		protected void appendProperties(StateFactory.Builder<Fluid, FluidState> var1)
		{
			super.appendProperties(var1);
			var1.with(LEVEL);
		}
		
		@Override
		public int method_15779(FluidState var1)
		{
			return var1.get(LEVEL);
		}
		
		@Override
		public boolean isStill(FluidState var1)
		{
			return false;
		}
	}
	
	public static class Still extends ExtendedWaterFluid
	{
		public Still(Function<Fluid, Fluid> still, Function<Fluid, Fluid> flowing, Function<Fluid, BlockState> blockState, Function<Fluid, Item> bucket)
		{
			super(still, flowing, blockState, bucket);
		}
		
		@Override
		public int method_15779(FluidState var1)
		{
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState var1)
		{
			return true;
		}
	}
}
