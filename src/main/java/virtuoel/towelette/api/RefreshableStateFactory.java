package virtuoel.towelette.api;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.MutableTriple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.MapUtil;
import virtuoel.towelette.util.FoamFixCompatibility;

public interface RefreshableStateFactory<O, S extends PropertyContainer<S>>
{
	default BiFunction<O, ImmutableMap<Property<?>, Comparable<?>>, S> getStateFunction()
	{
		return (o, m) -> null;
	}
	
	default Optional<Object> getFactory()
	{
		return Optional.empty();
	}
	
	default void setStates(ImmutableList<S> states)
	{
		
	}
	
	default Collection<S> refreshPropertyValues(final Property<?> property, final Collection<? extends Comparable<?>> newValues)
	{
		@SuppressWarnings("unchecked")
		final StateFactory<O, S> self = ((StateFactory<O, S>) (Object) this);
		
		final O baseObject = self.getBaseObject();
		final Collection<Property<?>> properties = self.getProperties();
		final ImmutableList<S> states = self.getStates();
		
		Stream<List<Comparable<?>>> tableStream = Stream.of(Collections.emptyList());
		
		for(final Property<?> entry : properties)
		{
			tableStream = tableStream.flatMap((propertyList) ->
			{
				return entry.getValues().stream().map((val) ->
				{
					final List<Comparable<?>> list = Lists.newArrayList(propertyList);
					list.add(val);
					return list;
				});
			});
		}
		
		final List<S> allStates = new LinkedList<>();
		final List<S> newStates = new LinkedList<>();
		
		final Map<Map<Property<?>, Comparable<?>>, S> stateMap = Maps.newLinkedHashMap();
		
		final BiFunction<O, ImmutableMap<Property<?>, Comparable<?>>, S> function = getStateFunction();
		
		final MutableTriple<Optional<Field>, Optional<?>, ?> compatibilityData = FoamFixCompatibility.resetFactoryMapperData(getFactory());
		
		tableStream.forEach((valueList) ->
		{
			final Map<Property<?>, Comparable<?>> propertyValueMap = MapUtil.createMap(properties, valueList);
			
			final S currentState;
			if(newValues.contains(propertyValueMap.get(property)))
			{
				currentState = function.apply(baseObject, ImmutableMap.copyOf(propertyValueMap));
				if(currentState != null)
				{
					newStates.add(currentState);
				}
				
				FoamFixCompatibility.loadFactoryMapperData(compatibilityData);
			}
			else
			{
				currentState = states.parallelStream().filter(state -> state.getEntries().equals(propertyValueMap)).findFirst().orElse(null);
			}
			
			if(currentState != null)
			{
				stateMap.put(propertyValueMap, currentState);
				allStates.add(currentState);
			}
		});
		
		if(!newStates.isEmpty())
		{
			allStates.parallelStream().forEach(propertyContainer ->
			{
				FoamFixCompatibility.setStateOwnerData(compatibilityData, propertyContainer);
				
				if(propertyContainer instanceof AbstractPropertyContainer<?, ?>)
				{
					@SuppressWarnings("unchecked")
					final AbstractPropertyContainer<?, S> abstractPropertyContainer = ((AbstractPropertyContainer<?, S>) propertyContainer);
					abstractPropertyContainer.createWithTable(stateMap);
				}
			});
			
			setStates(ImmutableList.copyOf(allStates));
		}
		
		return newStates;
	}
}
