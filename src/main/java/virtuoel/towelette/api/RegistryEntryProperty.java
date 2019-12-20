package virtuoel.towelette.api;

import net.minecraft.util.registry.Registry;

@Deprecated
public class RegistryEntryProperty<T> extends virtuoel.statement.api.property.RegistryEntryProperty<T>
{
	@Deprecated
	public RegistryEntryProperty(String name, Registry<T> registry)
	{
		super(name, registry);
	}
}
