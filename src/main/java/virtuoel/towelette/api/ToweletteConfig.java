package virtuoel.towelette.api;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;

public class ToweletteConfig
{
	public static final Supplier<JsonObject> HANDLER = Suppliers.memoize(JsonObject::new);
	
	public static final JsonObject DATA = HANDLER.get();
}
