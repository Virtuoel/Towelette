package virtuoel.towelette.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.AbstractProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.registry.Registry;
import virtuoel.towelette.api.FluidProperty.ComparableFluidWrapper;
import virtuoel.towelette.util.ReflectionUtil;

public class FluidProperty extends AbstractProperty<ComparableFluidWrapper>
{
	public static final FluidProperty FLUID = new FluidProperty();
	
	private FluidProperty()
	{
		super("fluid", ComparableFluidWrapper.class);
	}
	
	public ComparableFluidWrapper of(ItemPlacementContext context)
	{
		return of(context.getWorld().getFluidState(context.getPos()));
	}
	
	public ComparableFluidWrapper of(FluidState fluid)
	{
		return of(fluid.getFluid());
	}
	
	public ComparableFluidWrapper of(Fluid fluid)
	{
		if(valuesByName.isEmpty())
		{
			getValues();
		}
		
		ComparableFluidWrapper ret = valuesByName.get(new ComparableFluidWrapper(fluid).asString());
		if(ret == null)
		{
			ret = valuesByName.get(new ComparableFluidWrapper(Fluids.EMPTY).asString());
		}
		return ret;
	}
	
	public Fluid unwrap(BlockState state)
	{
		if(state.contains(this))
		{
			return state.get(this).wrapped;
		}
		else if(state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED))
		{
			return Fluids.WATER;
		}
		return Fluids.EMPTY;
	}
	
	public boolean tryAppendPropertySafely(StateFactory.Builder<Block, BlockState> var1)
	{
		if(ReflectionUtil.<Map<String, ?>, Boolean>test(map -> !map.containsKey(getName()), var1, ReflectionUtil.stateFactory$Builder$propertyMap, false))
		{
			var1.with(this);
			return true;
		}
		return false;
	}
	
	private Collection<ComparableFluidWrapper> values = new HashSet<>();
	private final Map<String, ComparableFluidWrapper> valuesByName = new HashMap<>();
	
	@Override
	public Collection<ComparableFluidWrapper> getValues()
	{
		if(values.isEmpty())
		{
			values.add(new ComparableFluidWrapper(Fluids.EMPTY));
			values.addAll(Registry.FLUID.keys().stream()
				.map(Registry.FLUID::get)
				.filter(f -> f.getDefaultState().isStill())
				.map(ComparableFluidWrapper::new)
				.collect(Collectors.toSet()));
			
			for(ComparableFluidWrapper f : values)
			{
				valuesByName.put(f.asString(), f);
			}
		}
		return values;
	}
	
	@Override
	public Optional<ComparableFluidWrapper> getValue(final String name)
	{
		return Optional.ofNullable(valuesByName.get(name));
	}
	
	@Override
	public String getValueAsString(ComparableFluidWrapper value)
	{
		return value.asString();
	}
	
	public static class ComparableFluidWrapper implements Comparable<ComparableFluidWrapper>, StringRepresentable
	{
		public final Fluid wrapped;
		
		public ComparableFluidWrapper(Fluid fluid)
		{
			this.wrapped = fluid;
		}
		
		public ComparableFluidWrapper(FluidState fluid)
		{
			this(fluid.getFluid());
		}
		
		@Override
		public int compareTo(ComparableFluidWrapper o)
		{
			return Registry.FLUID.getId(wrapped).compareTo(Registry.FLUID.getId(o.wrapped));
		}
		
		@Override
		public String asString()
		{
			return Registry.FLUID.getId(wrapped).toString().replace(":", "_f_");
		}
	}
}
