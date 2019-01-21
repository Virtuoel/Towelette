package virtuoel.towelette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.tags.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class Towelette implements ModInitializer
{
	public static final String MOD_ID = "towelette";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	public static final Tag<Block> DISPLACEABLE = TagRegistry.block(new Identifier(Towelette.MOD_ID, "displaceable"));
	public static final Tag<Block> UNDISPLACEABLE = TagRegistry.block(new Identifier(Towelette.MOD_ID, "undisplaceable"));
	
	@Override
	public void onInitialize()
	{
		
	}
	
	public static Identifier id(String name)
	{
		return new Identifier(MOD_ID, name);
	}
}
