package virtuoel.towelette.api;

import java.util.Collection;
import java.util.Collections;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public interface ToweletteApi
{
	public static final String MOD_ID = "towelette";
	
	public static final Collection<ToweletteApi> ENTRYPOINTS = FabricLoader.getInstance().getEntrypoints(MOD_ID, ToweletteApi.class);
	
	default Collection<Identifier> getBlacklistedFluidIds()
	{
		return Collections.emptyList();
	}
}
