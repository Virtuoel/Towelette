package virtuoel.towelette.fluid;

import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;

public abstract class ExtendedBaseFluid extends BaseFluid
{
	BlockState blockState;
	Function<Fluid, BlockState> blockStateSupplier;
	
	Fluid still;
	Function<Fluid, Fluid> stillSupplier;
	
	Fluid flowing;
	Function<Fluid, Fluid> flowingSupplier;
	
	Item bucket;
	Function<Fluid, Item> bucketSupplier;
	
	public ExtendedBaseFluid(Function<Fluid, Fluid> still, Function<Fluid, Fluid> flowing, Function<Fluid, BlockState> blockState, Function<Fluid, Item> bucket)
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
	
	@Override
	protected float getBlastResistance()
	{
		return 100.0F;
	}
	/*
	@Override
	protected BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	// is infinite?
	@Override
	protected boolean method_15737()
	{
		return false;
	}
	
	// flow into block?
	@Override
	protected void method_15730(IWorld var1, BlockPos var2, BlockState var3)
	{
		
	}
	
	// something about flow distance?
	@Override
	protected int method_15733(ViewableWorld var1)
	{
		return 4;
	}
	
	// something else about flow distance/level?
	@Override
	protected int method_15739(ViewableWorld var1)
	{
		return 0;
	}
	
	// some condition about flowing?
	@Override
	protected boolean method_15777(FluidState var1, Fluid var2, Direction var3)
	{
		return false;
	}
	
	// some sort of rate?
	@Override
	public int method_15789(ViewableWorld var1)
	{
		return 5;
	}
	*/
	public static abstract class Flowing extends ExtendedBaseFluid
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
	
	public static abstract class Still extends ExtendedBaseFluid
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
