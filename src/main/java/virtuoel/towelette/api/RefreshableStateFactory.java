package virtuoel.towelette.api;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.state.property.Property;

public interface RefreshableStateFactory<C>
{
	default Collection<C> refreshPropertyValues(final Property<?> property, final Collection<? extends Comparable<?>> newValues)
	{
		return new ArrayList<>();
	}
}
