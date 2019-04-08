package virtuoel.towelette.util;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.resource.Resource;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import virtuoel.towelette.Towelette;
import virtuoel.towelette.api.FluidProperty;

public class FluidUtils
{
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, ItemUsageContext context)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, context.getWorld().getFluidState(context.getBlockPos()));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, BlockView world, BlockPos pos)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, world.getFluidState(pos));
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, Fluid fluid)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, fluid.getDefaultState());
		}
		
		return state;
	}
	
	@Nullable
	public static BlockState getStateWithFluid(@Nullable BlockState state, FluidState fluid)
	{
		if(state != null && state.contains(FluidProperty.FLUID))
		{
			return getStateWithFluidImpl(state, fluid);
		}
		
		return state;
	}
	
	private static BlockState getStateWithFluidImpl(@Nonnull BlockState state, FluidState fluid)
	{
		final boolean isDoubleSlab = state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE;
		if(state.contains(Properties.WATERLOGGED))
		{
			state = state.with(Properties.WATERLOGGED, !isDoubleSlab && fluid.getFluid() == Fluids.WATER);
		}
		return state.with(FluidProperty.FLUID, isDoubleSlab ? FluidProperty.FLUID.of(Fluids.EMPTY) : FluidProperty.FLUID.of(fluid));
	}
	
	public static boolean scheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		return scheduleFluidTick(FluidProperty.FLUID.getFluid(state), world, pos);
	}
	
	public static boolean scheduleFluidTick(FluidState state, IWorld world, BlockPos pos)
	{
		if(!state.isEmpty())
		{
			scheduleFluidTickImpl(state.getFluid(), world, pos);
			return true;
		}
		return false;
	}
	
	public static boolean scheduleFluidTick(Fluid fluid, IWorld world, BlockPos pos)
	{
		if(!fluid.getDefaultState().isEmpty())
		{
			scheduleFluidTickImpl(fluid, world, pos);
			return true;
		}
		return false;
	}
	
	private static void scheduleFluidTickImpl(Fluid fluid, IWorld world, BlockPos pos)
	{
		world.getFluidTickScheduler().schedule(pos, fluid, fluid.getTickRate(world));
	}
	
	public static Identifier getIdForStillFluid(Fluid fluid)
	{
		if(!fluid.getDefaultState().isStill() && fluid instanceof BaseFluid)
		{
			fluid = ((BaseFluid) fluid).getStill();
		}
		return Registry.FLUID.getId(fluid);
	}
	
	public static class Client
	{
		public static final Sprite MISSING_SPRITE = MissingSprite.getMissingSprite();
		
		public static Identifier withBlockPath(Identifier id)
		{
			return new Identifier(id.getNamespace(), "block/" + id.getPath());
		}
		
		public static Identifier toTexturePath(Identifier id)
		{
			return new Identifier(id.getNamespace(), "textures/" + id.getPath() + ".png");
		}
		
		public static Optional<Identifier> getSpriteIdForTexture(Identifier id)
		{
			Identifier path = toTexturePath(id);
			try(Resource res = MinecraftClient.getInstance().getResourceManager().getResource(path))
			{
				return Optional.of(id);
			}
			catch(IOException e)
			{
				Towelette.LOGGER.debug("Could not load sprite {} from {}", id, path);
				return Optional.empty();
			}
		}
		
		public static Optional<Identifier> getSpriteIdForFluid(Fluid fluid, String suffix)
		{
			Identifier id = withBlockPath(new Identifier(getIdForStillFluid(fluid).toString() + suffix));
			
			return getSpriteIdForTexture(id);
		}
		
		public static Optional<Identifier> getSpriteIdForFluid(Fluid fluid, boolean still)
		{
			return getSpriteIdForFluid(fluid, still ? "_still" : "_flow");
		}
		
		public static Sprite getSpriteForFluid(Fluid fluid, boolean still)
		{
			return getSprite(getSpriteIdForFluid(fluid, still));
		}
		
		public static Optional<Identifier> getOverlaySpriteIdForFluid(Fluid fluid)
		{
			return getSpriteIdForFluid(fluid, "_overlay");
		}
		
		public static Sprite getOverlaySpriteForFluid(Fluid fluid)
		{
			return getSprite(getOverlaySpriteIdForFluid(fluid));
		}
		
		public static Sprite getSprite(Optional<Identifier> id)
		{
			return id.map(MinecraftClient.getInstance().getSpriteAtlas()::getSprite).orElse(MISSING_SPRITE);
		}
	}
}
