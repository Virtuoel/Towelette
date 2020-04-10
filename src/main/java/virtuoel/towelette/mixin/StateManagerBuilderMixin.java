package virtuoel.towelette.mixin;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.state.AbstractState;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.api.ToweletteConfig;

@Mixin(value = StateManager.Builder.class, priority = 990)
public abstract class StateManagerBuilderMixin<O, S extends State<S>>
{
	@Shadow @Final @Mutable O owner;
	@Shadow @Final @Mutable Map<String, Property<?>> namedProperties;
	
	@Shadow
	abstract StateManager.Builder<O, S> add(Property<?>... properties);
	
	@Inject(at = @At("HEAD"), method = "build(Lnet/minecraft/state/StateManager$Factory;)Lnet/minecraft/state/StateManager;")
	private <A extends AbstractState<O, S>> void onBuild(StateManager.Factory<O, S, A> factory, CallbackInfoReturnable<StateManager<O, S>> info)
	{
		if (owner instanceof Fluidloggable && !(owner instanceof TallSeagrassBlock))
		{
			if (!namedProperties.containsKey(FluidProperties.FLUID.getName()))
			{
				add(FluidProperties.FLUID);
			}
			
			if (Optional.ofNullable(ToweletteConfig.DATA.get("flowingFluidlogging"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(false)
			)
			{
				if (!namedProperties.containsKey(FluidProperties.LEVEL_1_8.getName()))
				{
					add(FluidProperties.LEVEL_1_8);
				}
				
				if (!namedProperties.containsKey(FluidProperties.FALLING.getName()))
				{
					add(FluidProperties.FALLING);
				}
			}
		}
	}
}
