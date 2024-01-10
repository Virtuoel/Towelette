package virtuoel.towelette.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import virtuoel.statement.util.VersionUtils;
import virtuoel.towelette.Towelette;

public class MaterialUtils
{
	public static final Object METAL;
	public static final Method IS_SOLID, IS_REPLACEABLE, GET_MATERIAL;

	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<Method> m = new Int2ObjectArrayMap<Method>();
		
		String mapped = "unset";
		Object o = null;
		
		try
		{
			if (VersionUtils.MINOR <= 19)
			{
				mapped = mappingResolver.mapClassName("intermediary", "net.minecraft.class_3614");
				Class<?> materialClass = Class.forName(mapped);
				
				mapped = mappingResolver.mapFieldName("intermediary", "net.minecraft.class_3614", "field_15953", "Lnet/minecraft/class_3614;");
				o = materialClass.getField(mapped).get(null);
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_3614", "method_15799", "()Z");
				m.put(0, materialClass.getMethod(mapped));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_3614", "method_15800", "()Z");
				m.put(1, materialClass.getMethod(mapped));
				
				mapped = mappingResolver.mapMethodName("intermediary", VersionUtils.MINOR <= 15 ? "net.minecraft.class_2248" : "net.minecraft.class_4970.class_4971", VersionUtils.MINOR <= 15 ? "method_9597" : "method_26207", "()Lnet/minecraft/class_3614;");
				m.put(2, (VersionUtils.MINOR <= 15 ? BlockState.class : AbstractBlock.AbstractBlockState.class).getMethod(mapped));
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e)
		{
			Towelette.LOGGER.error("Current name lookup: {}", mapped);
			Towelette.LOGGER.catching(e);
		}
		
		METAL = o;
		IS_SOLID = m.get(0);
		IS_REPLACEABLE = m.get(1);
		GET_MATERIAL = m.get(2);
	}
	
	public static boolean isSolid(Object material)
	{
		if (IS_SOLID != null)
		{
			try
			{
				return (boolean) IS_SOLID.invoke(material);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		throw new IllegalStateException();
	}
	
	public static boolean isReplaceable(Object material)
	{
		if (IS_REPLACEABLE != null)
		{
			try
			{
				return (boolean) IS_REPLACEABLE.invoke(material);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		throw new IllegalStateException();
	}
	
	public static final Object getMaterial(BlockState state)
	{
		if (GET_MATERIAL != null)
		{
			try
			{
				return GET_MATERIAL.invoke(state);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return null;
	}
}
