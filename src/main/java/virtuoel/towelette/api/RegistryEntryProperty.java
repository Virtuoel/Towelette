package virtuoel.towelette.api;

import java.util.Optional;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

public class RegistryEntryProperty<T> extends IdentifierProperty
{
	private final Registry<T> registry;
	private final Identifier defaultId;
	
	public RegistryEntryProperty(String name, Registry<T> registry)
	{
		super(name);
		
		this.registry = registry;
		
		if(this.registry instanceof DefaultedRegistry)
		{
			this.defaultId = ((DefaultedRegistry<T>) this.registry).getDefaultId();
			add(this.defaultId);
		}
		else
		{
			this.defaultId = null;
		}
	}
	
	public Registry<T> getRegistry()
	{
		return registry;
	}
	
	@Override
	public Optional<Identifier> parse(final String valueName)
	{
		return Optional.of(super.parse(valueName).flatMap(registry::getOrEmpty).map(registry::getId).orElse(defaultId));
	}
}
