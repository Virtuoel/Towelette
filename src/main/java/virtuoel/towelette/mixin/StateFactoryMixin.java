package virtuoel.towelette.mixin;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.state.PropertyContainer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import virtuoel.towelette.api.RefreshableStateFactory;

@Mixin(StateFactory.class)
public class StateFactoryMixin<O, S extends PropertyContainer<S>> implements RefreshableStateFactory<O, S>
{
	@Shadow @Final @Mutable ImmutableList<S> states;
	
	@Unique StateFactory.Factory<O, S, ?> factory;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	public void onConstruct(Object object_1, StateFactory.Factory<O, S, ?> stateFactory$Factory_1, Map<String, Property<?>> map_1, CallbackInfo info)
	{
		factory = stateFactory$Factory_1;
	}
	
	@Override
	public Optional<Object> getFactory()
	{
		return Optional.of(factory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BiFunction<O, ImmutableMap<Property<?>, Comparable<?>>, S> getStateFunction()
	{
		return (o, m) -> (S) factory.create(o, m);
	}
	
	@Override
	public void setStates(ImmutableList<S> states)
	{
		this.states = states;
	}
}
