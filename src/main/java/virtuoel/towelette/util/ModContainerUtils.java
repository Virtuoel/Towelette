package virtuoel.towelette.util;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import it.unimi.dsi.fastutil.objects.Object2BooleanAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;

public class ModContainerUtils
{
	private static final Object2BooleanMap<String> USER_ADDED_IDS_CACHE = new Object2BooleanAVLTreeMap<>();
	
	public static boolean isModUserAdded(final String modId)
	{
		synchronized (USER_ADDED_IDS_CACHE)
		{
			if (USER_ADDED_IDS_CACHE.containsKey(modId))
			{
				return USER_ADDED_IDS_CACHE.getBoolean(modId);
			}
			
			final ModContainer c = FabricLoader.getInstance().getModContainer(modId).get();
			
			if (c == null || c.getOrigin().getKind() == ModOrigin.Kind.NESTED)
			{
				USER_ADDED_IDS_CACHE.put(modId, false);
				return false;
			}
			else if (c.getOrigin().getKind() == ModOrigin.Kind.PATH)
			{
				USER_ADDED_IDS_CACHE.put(modId, true);
				return true;
			}
			
			try
			{
				final Path gameDir = FabricLoader.getInstance().getGameDir().toRealPath();
				final FileSystem gameFileSystem = gameDir.getFileSystem();
				final boolean isDevEnv = FabricLoader.getInstance().isDevelopmentEnvironment();
				
				boolean isModDevEnv, inModsFolder;
				Path containerPath;
				FileSystem containerFileSystem;
				for (final Path rootPath : c.getRootPaths())
				{
					containerPath = rootPath.toRealPath();
					containerFileSystem = containerPath.getFileSystem();
					isModDevEnv = isDevEnv && gameFileSystem == containerFileSystem;
					inModsFolder = containerFileSystem.toString().startsWith(gameDir.toString());
					
					if (isModDevEnv || inModsFolder)
					{
						USER_ADDED_IDS_CACHE.put(modId, true);
						return true;
					}
				}
			}
			catch (IOException e)
			{
				
			}
			
			return false;
		}
	}
}
