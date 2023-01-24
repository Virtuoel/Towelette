package virtuoel.towelette.init;

import java.util.Optional;

import virtuoel.towelette.Towelette;
import virtuoel.towelette.util.TagCompatibility;

public class TagRegistrar
{
	public static final Optional<Object> DISPLACEABLE = TagCompatibility.getBlockTag(Towelette.id("displaceable"));
	public static final Optional<Object> UNDISPLACEABLE = TagCompatibility.getBlockTag(Towelette.id("undisplaceable"));
}
