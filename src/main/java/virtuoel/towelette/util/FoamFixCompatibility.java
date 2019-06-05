package virtuoel.towelette.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.state.property.Property;

public class FoamFixCompatibility
{
	public static final Optional<Map<Property<?>, ?>> PROPERTY_ENTRY_MAP = getEntryMap();
	
	@SuppressWarnings("unchecked")
	private static Optional<Map<Property<?>, ?>> getEntryMap()
	{
		if(FabricLoader.getInstance().isModLoaded("foamfix"))
		{
			try
			{
				for(Field f : Class.forName("pl.asie.foamfix.state.PropertyOrdering").getDeclaredFields())
				{
					if(Map.class.isAssignableFrom(f.getType()))
					{
						f.setAccessible(true);
						return Optional.of((Map<Property<?>, ?>) f.get(null));
					}
				}
			}
			catch(SecurityException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException e)
			{
				
			}
		}
		return Optional.empty();
	}
}
