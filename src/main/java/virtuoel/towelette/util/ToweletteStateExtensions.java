package virtuoel.towelette.util;

import com.google.common.collect.ImmutableMap;

public interface ToweletteStateExtensions
{
	ImmutableMap<?, Comparable<?>> towelette_getEntries();
	
	default <T extends Comparable<T>, V extends T> ToweletteStateExtensions towelette_with(Object property, V value)
	{
		return this;
	}
	
	<T extends Comparable<T>> T towelette_get(Object property);
	
	@SuppressWarnings("unchecked")
	default <S> S towelette_cast()
	{
		return (S) this;
	}
}
