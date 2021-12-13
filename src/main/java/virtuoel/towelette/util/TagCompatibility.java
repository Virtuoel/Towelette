package virtuoel.towelette.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import virtuoel.statement.util.ReflectionUtils;

public class TagCompatibility
{
	private static final Optional<Class<?>> TAG_REGISTRY = ReflectionUtils.getClass("net.fabricmc.fabric.api.tag.TagRegistry");
	private static final Optional<Method> TAG_REGISTRY_BLOCK = ReflectionUtils.getMethod(TAG_REGISTRY, "block", Identifier.class);
	private static final Optional<Method> TAG_REGISTRY_FLUID = ReflectionUtils.getMethod(TAG_REGISTRY, "fluid", Identifier.class);
	
	public static class FluidTags
	{
		public static final Tag<Fluid> WATER = getFluidTag(new Identifier("minecraft", "water"));
		public static final Tag<Fluid> LAVA = getFluidTag(new Identifier("minecraft", "lava"));
	}
	
	public static Tag<Block> getBlockTag(Identifier id)
	{
		return getTag(id, () -> () -> TagFactory.BLOCK.create(id), TAG_REGISTRY_BLOCK);
	}
	
	public static Tag<Fluid> getFluidTag(Identifier id)
	{
		return getTag(id, () -> () -> TagFactory.FLUID.create(id), TAG_REGISTRY_FLUID);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Tag<T> getTag(Identifier id, Supplier<Supplier<Tag<T>>> tagFunc, Optional<Method> oldMethod)
	{
		try
		{
			return tagFunc.get().get();
		}
		catch (Exception e)
		{
			return oldMethod.map(m -> {
				try
				{
					return (Tag<T>) (Object) m.invoke(null, id);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
				{
					return null;
				}
			})
			.orElseThrow();
		}
	}
}
