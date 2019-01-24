package virtuoel.towelette.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import virtuoel.towelette.util.ReflectionUtil;

public class FluidProperty extends AbstractProperty<Identifier>
{
	public static final FluidProperty FLUID = new FluidProperty("fluid");
	
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
		return Registry.FLUID.getId(isValid(fluid) ? fluid : Fluids.EMPTY);
	}
	
	public boolean isValid(FluidState fluid)
	{
		return isValid(fluid.getFluid());
	}
	
	public boolean isValid(Fluid fluid)
	{
		return getValues().contains(Registry.FLUID.getId(fluid));
	}
	
	public FluidState getFluidState(BlockState state)
	{
		return getFluidState(getFluid(state));
	}
	
	public FluidState getFluidState(Fluid fluid)
	{
		if(fluid instanceof BaseFluid)
		{
			return ((BaseFluid) fluid).getState(false);
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
	
	public boolean tryAppendPropertySafely(StateFactory.Builder<Block, BlockState> builder)
	{
		if(ReflectionUtil.<Map<String, ?>, Boolean>test(map -> !map.containsKey(getName()), builder, ReflectionUtil.stateFactory$Builder$propertyMap, false))
		{
			builder.with(this);
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
			values.add(Registry.FLUID.getId(Fluids.EMPTY));
			values.addAll(Registry.FLUID.stream()
				.filter(f -> f.getDefaultState().isStill())
				.map(Registry.FLUID::getId)
				.collect(Collectors.toList()));
		}
		
		return values;
	}
	
	@Override
	public Optional<Identifier> getValue(final String name)
	{
		Identifier id = of(Fluids.EMPTY);
		
		final int underscoreIndex = name.lastIndexOf('_');
		if(underscoreIndex != -1)
		{
			try
			{
				final int namespaceLength = Integer.parseInt(name.substring(underscoreIndex + 1));
				id = new Identifier(name.substring(0, namespaceLength), name.substring(namespaceLength + 1, underscoreIndex));
				id = Registry.FLUID.getId(Registry.FLUID.get(id));
			}
			catch (NumberFormatException e)
			{
				
			}
		}
		
		return Optional.of(id);
	}
	
	@Override
	public String getValueAsString(Identifier value)
	{
		return value.getNamespace() + "_" + value.getPath() + "_" + value.getNamespace().length();
	}
}
