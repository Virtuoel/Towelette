package virtuoel.towelette.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.MutableTriple;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.property.Property;

public class FoamFixCompatibility
{
	private static final boolean FOAMFIX_LOADED = FabricLoader.getInstance().isModLoaded("foamfix");
	
	private static final Optional<Class<?>> ORDERING_CLASS = FOAMFIX_LOADED ? getClass("pl.asie.foamfix.state.PropertyOrdering") : Optional.empty();
	private static final Optional<Class<?>> FACTORY_CLASS = FOAMFIX_LOADED ? getClass("pl.asie.foamfix.state.FoamyStateFactory$Factory") : Optional.empty();
	private static final Optional<Class<?>> STATE_CLASS = FOAMFIX_LOADED ? getClass("pl.asie.foamfix.state.FoamyBlockStateMapped") : Optional.empty();
	
	private static final Optional<Map<Property<?>, ?>> PROPERTY_ENTRY_MAP = getEntryMap();
	
	private static final Optional<Field> FACTORY_MAPPER = getField(FACTORY_CLASS, "mapper");
	private static final Optional<Field> STATE_OWNER = getField(STATE_CLASS, "owner");
	
	public static void removePropertyFromEntryMap(Property<?> property)
	{
		PROPERTY_ENTRY_MAP.ifPresent(map ->
		{
			map.remove(property);
		});
	}
	
	public static MutableTriple<Optional<Field>, Optional<?>, ?> resetFactoryMapperData(final Optional<Object> factory)
	{
		final MutableTriple<Optional<Field>, Optional<?>, Object> data = MutableTriple.of(Optional.empty(), Optional.empty(), Optional.empty());
		factory.ifPresent(f ->
		{
			data.setRight(factory);
			
			final Optional<Field> mapper = FACTORY_CLASS.filter(c -> c.isInstance(f)).flatMap(c -> FACTORY_MAPPER);
			mapper.ifPresent(field ->
			{
				data.setLeft(mapper);
				try
				{
					field.set(f, null);
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					
				}
			});
		});
		return data;
	}
	
	public static void loadFactoryMapperData(final MutableTriple<Optional<Field>, Optional<?>, ?> data)
	{
		if(!data.getMiddle().isPresent())
		{
			data.getLeft().ifPresent(field ->
			{
				try
				{
					data.setMiddle(Optional.ofNullable(field.get(data.getRight())));
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					data.setMiddle(Optional.empty());
				}
			});
		}
	}
	
	public static void setStateOwnerData(final MutableTriple<Optional<Field>, Optional<?>, ?> data, final PropertyContainer<?> propertyContainer)
	{
		data.getMiddle().ifPresent(m ->
		{
			STATE_CLASS.filter(c -> c.isInstance(propertyContainer)).flatMap(c -> STATE_OWNER).ifPresent(f ->
			{
				try
				{
					f.set(propertyContainer, m);
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					
				}
			});
		});
	}
	
	@SuppressWarnings("unchecked")
	private static Optional<Map<Property<?>, ?>> getEntryMap()
	{
		return getField(ORDERING_CLASS, "entryMap").map(f ->
		{
			try
			{
				return (Map<Property<?>, ?>) f.get(null);
			}
			catch(IllegalArgumentException | IllegalAccessException e)
			{
				
			}
			return null;
		});
	}
	
	private static Optional<Field> getField(final Optional<Class<?>> classObj, final String fieldName)
	{
		return classObj.map(c ->
		{
			try
			{
				final Field f = c.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			}
			catch(SecurityException | NoSuchFieldException e)
			{
				
			}
			return null;
		});
	}
	
	private static Optional<Class<?>> getClass(final String className)
	{
		try
		{
			return Optional.of(Class.forName(className));
		}
		catch(ClassNotFoundException e)
		{
			
		}
		return Optional.empty();
	}
}
