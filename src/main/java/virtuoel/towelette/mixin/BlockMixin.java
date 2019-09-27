package virtuoel.towelette.mixin;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;appendProperties(Lnet/minecraft/state/StateFactory$Builder;)V"))
	public StateFactory.Builder<Block, BlockState> onConstructAppendPropertiesProxy(StateFactory.Builder<Block, BlockState> builder)
	{
		if(this instanceof Fluidloggable)
		{
			final Map<String, Property<?>> propertyMap = ((StateFactoryBuilderAccessor) builder).getPropertyMap();
			
			if(!propertyMap.containsKey(FluidProperties.FLUID.getName()))
			{
				builder.add(FluidProperties.FLUID);
			}
			
			if(Optional.ofNullable(ToweletteConfig.DATA.get("flowingFluidlogging"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(false))
			{
				if(!propertyMap.containsKey(FluidProperties.LEVEL_1_8.getName()))
				{
					builder.add(FluidProperties.LEVEL_1_8);
				}
				
				if(!propertyMap.containsKey(FluidProperties.FALLING.getName()))
				{
					builder.add(FluidProperties.FALLING);
				}
			}
		}
		
		return builder;
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
