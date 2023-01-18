package virtuoel.towelette.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import virtuoel.statement.util.VersionUtils;
import virtuoel.towelette.Towelette;

public class RegistryUtils extends virtuoel.statement.util.RegistryUtils
{
	public static final MethodHandle GET_IDS;
	public static final Object BLOCK_REGISTRY_KEY;
	public static final Object FLUID_REGISTRY_KEY;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<MethodHandle> h = new Int2ObjectArrayMap<MethodHandle>();
		Object kB, kF = kB = null;
		
		final Lookup lookup = MethodHandles.lookup();
		String mapped = "unset";
		Method m;
		Class<?> clazz;
		Field f;
		
		try
		{
			final boolean is116Plus = VersionUtils.MINOR >= 16;
			final boolean is1192Minus = VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 2);
			
			if (is116Plus)
			{
				final String keyRegistrar = "net.minecraft.class_" + (is1192Minus ? "2378" : "7924");
				
				mapped = mappingResolver.mapClassName("intermediary", keyRegistrar);
				clazz = Class.forName(mapped);
				
				mapped = mappingResolver.mapFieldName("intermediary", keyRegistrar, "field_" + (is1192Minus ? "25105" : "41254"), "Lnet/minecraft/class_5321;");
				f = clazz.getField(mapped);
				kB = f.get(null);
				
				mapped = mappingResolver.mapFieldName("intermediary", keyRegistrar, "field_" + (is1192Minus ? "25103" : "41270"), "Lnet/minecraft/class_5321;");
				f = clazz.getField(mapped);
				kF = f.get(null);
			}
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2378", "method_10235", "()Ljava/util/Set;");
			m = Registry.class.getMethod(mapped);
			h.put(0, lookup.unreflect(m));
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e)
		{
			Towelette.LOGGER.error("Current name lookup: {}", mapped);
			Towelette.LOGGER.catching(e);
		}
		
		GET_IDS = h.get(0);
		BLOCK_REGISTRY_KEY = kB;
		FLUID_REGISTRY_KEY = kF;
	}
	
	public static <V> Set<Identifier> getIds(Registry<V> registry)
	{
		try
		{
			return (Set<Identifier>) GET_IDS.invoke(registry);
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
}
