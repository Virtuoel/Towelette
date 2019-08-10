package virtuoel.towelette.api;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.minecraft.util.Lazy;

public class ToweletteConfig
{
	public static final Supplier<JsonObject> HANDLER = new Lazy<JsonObject>(JsonObject::new)::get;
	
	public static final JsonObject DATA = HANDLER.get();
}
