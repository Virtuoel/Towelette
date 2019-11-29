package virtuoel.towelette.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;

@Mixin(StateManager.Builder.class)
public interface StateManagerBuilderAccessor
{
	@Accessor
	Map<String, Property<?>> getNamedProperties();
}
