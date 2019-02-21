package virtuoel.towelette.util;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
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
		if(state.contains(Properties.WATERLOGGED) && fluid.getFluid() != Fluids.WATER)
		{
			state = state.with(Properties.WATERLOGGED, false);
		}
		return state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluid));
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
