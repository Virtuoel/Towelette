package virtuoel.towelette.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonConfigHandler extends ConfigHandler<JsonObject>
{
	public JsonConfigHandler(String modId, String path, Supplier<JsonObject> defaultConfig)
	{
		super(modId, path + ".json", defaultConfig,
			JsonConfigHandler::readConfig,
			JsonConfigHandler::writeConfig
		);
	}
	
	private static JsonObject readConfig(Reader reader)
	{
		return Streams.parse(new JsonReader(reader)).getAsJsonObject();
	}
	
	private static void writeConfig(Writer writer, JsonObject configData)
	{
		try
		{
			final JsonWriter jsonWriter = new JsonWriter(writer);
			jsonWriter.setIndent("\t");
			Streams.write(configData, jsonWriter);
			writer.write('\n');
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
