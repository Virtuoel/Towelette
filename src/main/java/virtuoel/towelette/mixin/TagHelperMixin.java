package virtuoel.towelette.mixin;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.property.Property;
import net.minecraft.util.TagHelper;

@Mixin(TagHelper.class)
public class TagHelperMixin
{
	@Shadow static final Logger LOGGER = LogManager.getLogger();
	
	@Redirect(method = "deserializeBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/TagHelper;withProperty(Lnet/minecraft/state/PropertyContainer;Lnet/minecraft/state/property/Property;Ljava/lang/String;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/state/PropertyContainer;"))
	private static <S extends PropertyContainer<S>, T extends Comparable<T>> S deserializeBlockStateWithPropertyProxy(S container, Property<T> property, String key, CompoundTag properties, CompoundTag compound)
	{
		try
		{
			return withProperty(container, property, key, properties, compound);
		}
		catch (IllegalArgumentException e)
		{
			LOGGER.warn(e.getMessage());
			return container;
		}
	}
	
	@Shadow
	private static <S extends PropertyContainer<S>, T extends Comparable<T>> S withProperty(S container, Property<T> property, String key, CompoundTag properties, CompoundTag compound)
	{
		Optional<T> value = property.getValue(properties.getString(key));
		if(value.isPresent())
		{
			return container.with(property, value.get());
		}
		else
		{
			LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", key, properties.getString(key), compound.toString());
			return container;
		}
	}
}
