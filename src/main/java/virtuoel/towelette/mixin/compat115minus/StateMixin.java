package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableMap;

import net.minecraft.state.property.Property;
import virtuoel.towelette.util.ToweletteStateExtensions;

@Mixin(targets = "net.minecraft.class_2688", remap = false)
public interface StateMixin<C> extends ToweletteStateExtensions
{
	@Shadow(remap = false) <T extends Comparable<T>> T method_11654(Property<?> property);
	@Shadow(remap = false) <T extends Comparable<T>, V extends T> C method_11657(Property<?> property, V value);
	@Shadow(remap = false) ImmutableMap<Property<?>, Comparable<?>> method_11656();
	
	@Override
	default <T extends Comparable<T>> T towelette_get(Object property)
	{
		return method_11654((Property<?>) property);
	}
	
	@Override
	default ImmutableMap<?, Comparable<?>> towelette_getEntries()
	{
		return method_11656();
	}
	
	@Override
	default <T extends Comparable<T>, V extends T> ToweletteStateExtensions towelette_with(Object property, V value)
	{
		return (ToweletteStateExtensions) method_11657((Property<?>) property, value);
	}
}