package virtuoel.towelette.mixin;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.state.StateFactory;

@Mixin(StateFactory.Builder.class)
public abstract class StateFactoryBuilderMixin
{
	@Redirect(method = "validate", at = @At(value = "INVOKE", target = "Ljava/util/Collection;size()I", remap = false))
	public <T extends Comparable<T>> int validateSizeProxy(Collection<T> obj)
	{
		final int size = obj.size();
		return size == 1 ? 2 : size;
	}
}
