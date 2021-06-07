package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableMap;

import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import virtuoel.towelette.util.ToweletteStateExtensions;

@Mixin(State.class)
public abstract class StateMixin<C> implements ToweletteStateExtensions
{
	@Shadow abstract <T extends Comparable<T>> T get(Property<?> property);
	@Shadow abstract <T extends Comparable<T>, V extends T> C with(Property<?> property, V value);
	@Shadow abstract ImmutableMap<Property<?>, Comparable<?>> getEntries();
	
	@Override
	public <T extends Comparable<T>> T towelette_get(Object property)
	{
		return get((Property<?>) property);
	}
	
	@Override
	public ImmutableMap<?, Comparable<?>> towelette_getEntries()
	{
		return getEntries();
	}
	
	@Override
	public <T extends Comparable<T>, V extends T> ToweletteStateExtensions towelette_with(Object property, V value)
	{
		return (ToweletteStateExtensions) with((Property<?>) property, value);
	}
}