package virtuoel.towelette.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.minecraft.state.property.AbstractProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidProperty extends AbstractProperty<Identifier>
{
	public static final FluidProperty FLUID = new FluidProperty("fluid");
	
	private FluidProperty(String name)
	{
		super(name, Identifier.class);
		values.add(Registry.FLUID.getDefaultId());
	}
	
	private final List<Identifier> values = new ArrayList<>();
	
	@Override
	public Collection<Identifier> getValues()
	{
		return values;
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
		
		return Optional.of(Registry.FLUID.getDefaultId());
	}
	
	@Override
	public String getName(Identifier value)
	{
		return value.getNamespace() + "_" + value.getPath() + "_" + value.getNamespace().length();
	}
}
