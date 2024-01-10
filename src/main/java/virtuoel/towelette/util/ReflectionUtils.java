package virtuoel.towelette.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import virtuoel.statement.util.VersionUtils;
import virtuoel.towelette.Towelette;

public class ReflectionUtils
{
	public static final Object METAL;
	public static final Method IS_RECEIVING_REDSTONE_POWER, IS_SOLID, IS_REPLACEABLE, GET_MATERIAL, CAN_FILL_WITH_FLUID, GET_FLUID_TICK_SCHEDULER;

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
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1937", "method_8479", "(Lnet/minecraft/class_2338;)Z");
				m.put(0, World.class.getMethod(mapped, BlockPos.class));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_3614", "method_15799", "()Z");
				m.put(1, materialClass.getMethod(mapped));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_3614", "method_15800", "()Z");
				m.put(2, materialClass.getMethod(mapped));
				
				mapped = mappingResolver.mapMethodName("intermediary", VersionUtils.MINOR <= 15 ? "net.minecraft.class_2248" : "net.minecraft.class_4970.class_4971", VersionUtils.MINOR <= 15 ? "method_11620" : "method_26207", "()Lnet/minecraft/class_3614;");
				m.put(3, (VersionUtils.MINOR <= 15 ? BlockState.class : AbstractBlock.AbstractBlockState.class).getMethod(mapped));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2402", "method_10310", "(Lnet/minecraft/class_1922;Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_3611;)Z");
				m.put(4, FluidFillable.class.getMethod(mapped, BlockView.class, BlockPos.class, BlockState.class, Fluid.class));
			}
			
			if (VersionUtils.MINOR <= 17)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1936", "method_8405", "()Lnet/minecraft/class_1951;");
				m.put(5, WorldAccess.class.getMethod(mapped));
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e)
		{
			Towelette.LOGGER.error("Current name lookup: {}", mapped);
			Towelette.LOGGER.catching(e);
		}
		
		METAL = o;
		IS_RECEIVING_REDSTONE_POWER = m.get(0);
		IS_SOLID = m.get(1);
		IS_REPLACEABLE = m.get(2);
		GET_MATERIAL = m.get(3);
		CAN_FILL_WITH_FLUID = m.get(4);
		GET_FLUID_TICK_SCHEDULER = m.get(5);
	}
	
	public static boolean isReceivingRedstonePower(World world, BlockPos pos)
	{
		if (IS_RECEIVING_REDSTONE_POWER != null)
		{
			try
			{
				return (boolean) IS_RECEIVING_REDSTONE_POWER.invoke(world, pos);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return world.isReceivingRedstonePower(pos);
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
		
		throw new IllegalStateException();
	}
	
	public static Object getFluidTickScheduler(WorldAccess world)
	{
		if (GET_FLUID_TICK_SCHEDULER != null)
		{
			try
			{
				return GET_FLUID_TICK_SCHEDULER.invoke(world);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		throw new IllegalStateException();
	}
	
	public static boolean canFillWithFluid(@Nullable PlayerEntity playerEntity, FluidFillable block, BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		if (CAN_FILL_WITH_FLUID != null)
		{
			try
			{
				return (boolean) CAN_FILL_WITH_FLUID.invoke(block, world, pos, state, fluid);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return block.canFillWithFluid(playerEntity, world, pos, state, fluid);
	}
}
