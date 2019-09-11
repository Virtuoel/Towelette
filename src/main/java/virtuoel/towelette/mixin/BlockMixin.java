package virtuoel.towelette.mixin;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Shadow abstract void appendProperties(StateFactory.Builder<Block, BlockState> builder);
	
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;appendProperties(Lnet/minecraft/state/StateFactory$Builder;)V"))
	public void onConstructAppendPropertiesProxy(Block obj, StateFactory.Builder<Block, BlockState> builder)
	{
		appendProperties(builder);
		if(obj instanceof Fluidloggable)
		{
			final Map<String, Property<?>> propertyMap = ((StateFactoryBuilderAccessor) builder).getPropertyMap();
			
			if(!propertyMap.containsKey(FluidProperty.FLUID.getName()))
			{
				builder.add(FluidProperty.FLUID);
			}
			
			if(Optional.ofNullable(ToweletteConfig.DATA.get("flowingFluidlogging"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(false))
			{
				if(!propertyMap.containsKey(FluidProperty.LEVEL_1_8.getName()))
				{
					builder.add(FluidProperty.LEVEL_1_8);
				}
				
				if(!propertyMap.containsKey(FluidProperty.FALLING.getName()))
				{
					builder.add(FluidProperty.FALLING);
				}
			}
		}
	}
	
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;setDefaultState(Lnet/minecraft/block/BlockState;)V"))
	public BlockState onConstructSetDefaultState(BlockState state)
	{
		return FluidUtils.getStateWithFluid(state, Fluids.EMPTY.getDefaultState());
	}
	
	@Inject(at = @At("HEAD"), method = "getFluidState", cancellable = true)
	public void getFluidState(BlockState state, CallbackInfoReturnable<FluidState> info)
	{
		info.setReturnValue(FluidUtils.getFluidState(state));
	}
}
