package virtuoel.towelette;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import virtuoel.towelette.util.FluidUtils;

public class ToweletteClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientSpriteRegistryCallback.registerBlockAtlas((atlasTexture, registry) ->
		{
			registerFluidSprites(registry, Fluids.WATER);
			registerFluidSprites(registry, Fluids.FLOWING_WATER);
			registerFluidSprites(registry, Fluids.LAVA);
			registerFluidSprites(registry, Fluids.FLOWING_LAVA);
		});
	}
	
	public static void registerFluidSprites(ClientSpriteRegistryCallback.Registry registry, Fluid fluid)
	{
		FluidUtils.Client.getSpriteIdForFluid(fluid, true).ifPresent(registry::register);
		FluidUtils.Client.getSpriteIdForFluid(fluid, false).ifPresent(registry::register);
		FluidUtils.Client.getOverlaySpriteIdForFluid(fluid).ifPresent(registry::register);
	}
}
