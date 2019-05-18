package virtuoel.towelette.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigHandler<S>
{
	private final String modId;
	private final Logger logger;
	private final File configFile;
	private final Supplier<S> defaultConfig;
	private final Function<Reader, S> configReader;
	private final BiConsumer<Writer, S> configWriter;
	
	public ConfigHandler(String modId, String path, Supplier<S> defaultConfig, Function<Reader, S> configReader, BiConsumer<Writer, S> configWriter)
	{
		this.modId = modId;
		logger = LogManager.getLogger(modId);
		configFile = new File(FabricLoader.getInstance().getConfigDirectory(), path);
		this.defaultConfig = defaultConfig;
		this.configReader = configReader;
		this.configWriter = configWriter;
	}
	
	public String getModID()
	{
		return modId;
	}
	
	public S load()
	{
		return load(logger, configFile, defaultConfig, configReader, configWriter);
	}
	
	public static <T> T load(Logger logger, File configFile, Supplier<T> defaultConfig, Function<Reader, T> configReader, BiConsumer<Writer, T> configWriter)
	{
		T configData = null;
		configFile.getParentFile().mkdirs();
		if(configFile.exists())
		{
			try(final FileReader reader = new FileReader(configFile))
			{
				configData = configReader.apply(reader);
			}
			catch(IOException e)
			{
				logger.catching(e);
			}
		}
		if(configData == null)
		{
			configData = defaultConfig.get();
			save(logger, configData, configFile, configWriter);
		}
		return configData;
	}
	
	public void save(S configData)
	{
		save(logger, configData, configFile, configWriter);
	}
	
	public static <T> void save(Logger logger, T configData, File configFile, BiConsumer<Writer, T> configWriter)
	{
		try(final FileWriter writer = new FileWriter(configFile))
		{
			configWriter.accept(writer, configData);
		}
		catch(IOException e)
		{
			logger.warn("Failed to write config.");
			logger.catching(e);
		}
	}
}
