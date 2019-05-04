package virtuoel.towelette.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;

@Mixin(StateFactory.Builder.class)
public interface StateFactoryBuilderAccessor
{
	@Accessor
	Map<String, Property<?>> getPropertyMap();
}
