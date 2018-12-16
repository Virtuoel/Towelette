package virtuoel.towelette.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.state.StateFactory;

public class ReflectionUtil
{
	public static final Optional<Field> stateFactory$Builder$propertyMap =
		Stream.of(StateFactory.Builder.class.getDeclaredFields())
		.filter(f -> Map.class.isAssignableFrom(f.getType()))
		.<Field>map(f -> { f.setAccessible(true); return f;})
		.findFirst();
	
	public static <T, R> R test(Function<T, R> condition, Object obj, Optional<Field> field, R defaultReturn)
	{
		if(field.isPresent())
		{
			try
			{
				@SuppressWarnings("unchecked")
				T fieldContents = (T) field.get().get(obj);
				
				return condition.apply(fieldContents);
			}
			catch(IllegalAccessException e)
			{
				
			}
		}
		return defaultReturn;
	}
}
