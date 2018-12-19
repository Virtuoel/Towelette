package virtuoel.towelette;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.client.texture.SpriteRegistry;
import net.fabricmc.fabric.events.client.SpriteEvent;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import virtuoel.towelette.util.FluidUtils;

public class ToweletteClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		SpriteEvent.PROVIDE.register(registry ->
		{
			registerFluidSprites(registry, Fluids.WATER);
			registerFluidSprites(registry, Fluids.FLOWING_WATER);
			registerFluidSprites(registry, Fluids.LAVA);
			registerFluidSprites(registry, Fluids.FLOWING_LAVA);
		});
	}
	
	public static void registerFluidSprites(SpriteRegistry registry, Fluid fluid)
	{
		FluidUtils.getSpriteIdForFluid(fluid, true).ifPresent(registry::register);
		FluidUtils.getSpriteIdForFluid(fluid, false).ifPresent(registry::register);
		FluidUtils.getOverlaySpriteIdForFluid(fluid).ifPresent(registry::register);
	}
}
