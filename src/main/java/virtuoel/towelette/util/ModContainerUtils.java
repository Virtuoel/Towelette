package virtuoel.towelette.util;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class ModContainerUtils
{
	public static boolean isModUserAdded(final String modId)
	{
		try
		{
			final ModContainer c = FabricLoader.getInstance().getModContainer(modId).get();
			
			if (c == null)
			{
				return false;
			}
			
			final Path gameDir = FabricLoader.getInstance().getGameDir().toRealPath();
			final Path containerPath = c.getRootPath().toRealPath();
			final FileSystem gameFileSystem = gameDir.getFileSystem();
			final FileSystem containerFileSystem = containerPath.getFileSystem();
			
			boolean userAdded = false;
			
			final boolean isDevEnv = FabricLoader.getInstance().isDevelopmentEnvironment();
			final boolean isModDevEnv = isDevEnv && gameFileSystem == containerFileSystem;
			final boolean inModsFolder = containerFileSystem.toString().startsWith(gameDir.toString());
			userAdded = isModDevEnv || inModsFolder;
			
			return userAdded;
		}
		catch (IOException e)
		{
			
		}
		
		return false;
	}
}
