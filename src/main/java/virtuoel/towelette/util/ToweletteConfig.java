package virtuoel.towelette.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.loader.api.FabricLoader;
import virtuoel.towelette.Towelette;

public class ToweletteConfig
{
	private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDirectory(), Towelette.MOD_ID + "/config.json");
	
	public static JsonObject load(Supplier<JsonObject> defaultConfig)
	{
		return load(CONFIG_FILE, defaultConfig);
	}
	
	public static JsonObject load(File configFile, Supplier<JsonObject> defaultConfig)
	{
		JsonObject configData = null;
		configFile.getParentFile().mkdirs();
		if(configFile.exists())
		{
			try(FileReader reader = new FileReader(configFile))
			{
				configData = Streams.parse(new JsonReader(reader)).getAsJsonObject();
			}
			catch(JsonParseException | IOException | IllegalStateException e)
			{
				e.printStackTrace();
			}
		}
		if(configData == null)
		{
			configData = defaultConfig.get();
			save(configData, configFile);
		}
		return configData;
	}
	
	public static void save(JsonElement configData)
	{
		save(configData, CONFIG_FILE);
	}
	
	public static void save(JsonElement configData, File configFile)
	{
		try(FileWriter writer = new FileWriter(configFile))
		{
			JsonWriter jsonWriter = new JsonWriter(writer);
			jsonWriter.setIndent("\t");
			Streams.write(configData, jsonWriter);
			writer.write('\n');
		}
		catch(IOException e)
		{
			Towelette.LOGGER.warn("Failed to write config.");
			Towelette.LOGGER.catching(e);
		}
	}
}
