package virtuoel.towelette.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

import virtuoel.kanos_config.api.JsonConfigBuilder;
import virtuoel.statement.util.VersionUtils;
import virtuoel.towelette.api.ToweletteApi;
import virtuoel.towelette.api.ToweletteConfig;

public class BackwardsCompatibility
{
	public static final ILogger LOGGER = MixinService.getService().getLogger(ToweletteApi.MOD_ID);
	
	private static boolean APPLIED = false;
	
	public static void addFieldsIfNeeded(final ClassNode targetClass)
	{
		if (VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 3))
		{
			targetClass.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "DATA", "Lcom/google/gson/JsonObject;", null, null);
			APPLIED = true;
			LOGGER.debug("[Towelette] Applied backwards compatibility patch.");
		}
	}
	
	public static void populateFieldsIfNeeded(final JsonConfigBuilder builder)
	{
		if (APPLIED)
		{
			try
			{
				ToweletteConfig.class.getField("DATA").set(null, builder.config.get());
			}
			catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
			{
				LOGGER.catching(e);
				LOGGER.warn("[Towelette] Backwards compatibility patch failed. Older mods you have that use Towelette might not work.");
				return;
			}
			
			LOGGER.debug("[Towelette] Populated backwards compatibility fields.");
		}
	}
}
