package virtuoel.towelette.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.AbstractProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidProperty extends AbstractProperty<Identifier>
{
	public static final FluidProperty FLUID = new FluidProperty("fluid");
	private static final Identifier EMPTY_ID = Registry.FLUID.getId(Fluids.EMPTY);
	
	private FluidProperty(String name)
	{
		super(name, Identifier.class);
	}
	
	public Identifier of(ItemPlacementContext context)
	{
		return of(context.getWorld().getFluidState(context.getBlockPos()));
	}
	
	public Identifier of(FluidState fluid)
	{
		return of(fluid.getFluid());
	}
	
	public Identifier of(Fluid fluid)
	{
		final Identifier id = Registry.FLUID.getId(fluid);
		return isValid(id) ? id : EMPTY_ID;
	}
	
	public boolean isValid(FluidState fluid)
	{
		return isValid(fluid.getFluid());
	}
	
	public boolean isValid(Fluid fluid)
	{
		return isValid(Registry.FLUID.getId(fluid));
	}
	
	public boolean isValid(Identifier id)
	{
		return getValues().contains(id);
	}
	
	public FluidState getFluidState(BlockState state)
	{
		return getFluidState(getFluid(state));
	}
	
	public FluidState getFluidState(Fluid fluid)
	{
		if(fluid instanceof BaseFluid)
		{
			return ((BaseFluid) fluid).getStill(false);
		}
		return Fluids.EMPTY.getDefaultState();
	}
	
	public Fluid getFluid(BlockState state)
	{
		Fluid ret = Fluids.EMPTY;
		if(state != null)
		{
			if(state.contains(this))
			{
				ret = Registry.FLUID.get(state.get(this));
			}
			
			if(ret == Fluids.EMPTY && state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED))
			{
				ret = Fluids.WATER;
			}
		}
		return ret;
	}
	
	@Deprecated
	public boolean tryAppendPropertySafely(StateFactory.Builder<Block, BlockState> builder)
	{
		if(!((virtuoel.towelette.mixin.StateFactoryBuilderAccessor) builder).getPropertyMap().containsKey(getName()))
		{
			builder.add(this);
			return true;
		}
		return false;
	}
	
	private final List<Identifier> values = new ArrayList<>();
	
	@Override
	public Collection<Identifier> getValues()
	{
		if(values.isEmpty())
		{
			values.add(EMPTY_ID);
			values.addAll(Registry.FLUID.stream()
				.filter(this::filter)
				.map(Registry.FLUID::getId)
				.collect(Collectors.toList()));
		}
		
		return values;
	}
	
	public boolean filter(Fluid fluid)
	{
		return fluid.getDefaultState().isStill();
	}
	
	@Override
	public Optional<Identifier> getValue(final String name)
	{
		final int underscoreIndex = name.lastIndexOf('_');
		if(underscoreIndex != -1)
		{
			try
			{
				final int namespaceLength = Integer.parseInt(name.substring(underscoreIndex + 1));
				final Identifier id = new Identifier(name.substring(0, namespaceLength), name.substring(namespaceLength + 1, underscoreIndex));
				return Optional.of(Registry.FLUID.getId(Registry.FLUID.get(id)));
			}
			catch (NumberFormatException e)
			{
				
			}
		}
		
		return Optional.of(EMPTY_ID);
	}
	
	@Override
	public String getName(Identifier value)
	{
		return value.getNamespace() + "_" + value.getPath() + "_" + value.getNamespace().length();
	}
}
