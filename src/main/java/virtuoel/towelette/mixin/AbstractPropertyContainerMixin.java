package virtuoel.towelette.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.Table;

import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Property;

@Mixin(AbstractPropertyContainer.class)
public abstract class AbstractPropertyContainerMixin<O, S>
{
	@Redirect(method = "createWithTable", at = @At(value = "FIELD", ordinal = 0, target = "Lnet/minecraft/state/AbstractPropertyContainer;withTable:Lcom/google/common/collect/Table;"))
	public Table<Property<?>, Comparable<?>, S> onCreateWithTable(AbstractPropertyContainer<O, S> this$0, Map<Map<Property<?>, Comparable<?>>, S> map_1)
	{
		return null;
	}
}
