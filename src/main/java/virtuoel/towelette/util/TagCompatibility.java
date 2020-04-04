package virtuoel.towelette.util;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class TagCompatibility
{
	public static class FluidTags
	{
		public static final Tag<Fluid> WATER = TagRegistry.fluid(new Identifier("minecraft", "water"));
		public static final Tag<Fluid> LAVA = TagRegistry.fluid(new Identifier("minecraft", "lava"));
	}
}
