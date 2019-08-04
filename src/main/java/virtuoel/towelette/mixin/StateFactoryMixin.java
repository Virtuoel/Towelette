package virtuoel.towelette.mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.MapUtil;
import virtuoel.towelette.api.RefreshableStateFactory;

@Mixin(StateFactory.class)
public class StateFactoryMixin<O, S extends PropertyContainer<S>, A extends AbstractPropertyContainer<O, S>> implements RefreshableStateFactory<PropertyContainer<?>>
{
	@Shadow @Final @Mutable O baseObject;
	@Shadow @Final @Mutable ImmutableSortedMap<String, Property<?>> propertyMap;
	@Shadow @Final @Mutable ImmutableList<PropertyContainer<?>> states;
	
	@Unique StateFactory.Factory<O, S, A> factory;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	public void onConstruct(O object_1, StateFactory.Factory<O, S, A> stateFactory$Factory_1, Map<String, Property<?>> map_1, CallbackInfo info)
	{
		factory = stateFactory$Factory_1;
	}
	
	@Override
	public Collection<PropertyContainer<?>> refreshPropertyValues(final Property<?> property, final Collection<? extends Comparable<?>> newValues)
	{
		Stream<List<Comparable<?>>> tableStream = Stream.of(Collections.emptyList());
		
		for(final Property<?> entry : propertyMap.values())
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
		
		final List<PropertyContainer<?>> allStates = new LinkedList<>();
		final List<PropertyContainer<?>> newStates = new LinkedList<>();
		
		final Map<Map<Property<?>, Comparable<?>>, PropertyContainer<?>> stateMap = Maps.newLinkedHashMap();
		
		tableStream.forEach((valueList) ->
		{
			final Map<Property<?>, Comparable<?>> propertyValueMap = MapUtil.createMap(propertyMap.values(), valueList);
			
			PropertyContainer<?> currentState = null;
			if(newValues.contains(propertyValueMap.get(property)))
			{
				currentState = factory.create(baseObject, ImmutableMap.copyOf(propertyValueMap));
				newStates.add(currentState);
			}
			else
			{
				for(final PropertyContainer<?> state : states)
				{
					if(state.getEntries().equals(propertyValueMap))
					{
						currentState = state;
						break;
					}
				}
			}
			
			if(currentState != null)
			{
				stateMap.put(propertyValueMap, currentState);
				allStates.add(currentState);
			}
		});
		
		if(!newStates.isEmpty())
		{
			for(final PropertyContainer<?> propertyContainer : allStates)
			{
				createWithTable(propertyContainer, stateMap);
			}
			
			states = ImmutableList.copyOf(allStates);
		}
		
		return newStates;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Unique
	private static void createWithTable(PropertyContainer<?> propertyContainer, Map<Map<Property<?>, Comparable<?>>, ?> stateMap)
	{
		if(propertyContainer instanceof AbstractPropertyContainer<?, ?>)
		{
			((AbstractPropertyContainer) propertyContainer).createWithTable(stateMap);
		}
	}
}
