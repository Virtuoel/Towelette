package virtuoel.towelette.mixin;

import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateFactory;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.Property;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.api.StateFactoryRebuildable;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Block.class)
@Implements(@Interface(iface = StateFactoryRebuildable.class, prefix = "towelette$", remap = Remap.NONE))
public abstract class BlockMixin
{
	@Shadow @Mutable StateFactory<Block, BlockState> stateFactory;
	
	@Shadow abstract void setDefaultState(BlockState blockState_1);
	@Shadow abstract BlockState getDefaultState();
	
	@Shadow abstract void appendProperties(StateFactory.Builder<Block, BlockState> builder);
	
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;appendProperties(Lnet/minecraft/state/StateFactory$Builder;)V"))
	public void onConstructAppendPropertiesProxy(Block obj, StateFactory.Builder<Block, BlockState> builder)
	{
		appendProperties(builder);
		if(obj instanceof Fluidloggable)
		{
			if(!((StateFactoryBuilderAccessor) builder).getPropertyMap().containsKey(FluidProperty.FLUID.getName()))
			{
				builder.add(FluidProperty.FLUID);
			}
		}
	}
	
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;setDefaultState(Lnet/minecraft/block/BlockState;)V"))
	public BlockState onConstructSetDefaultState(BlockState state)
	{
		return FluidUtils.getStateWithFluid(state, Fluids.EMPTY);
	}
	
	@Inject(at = @At("HEAD"), method = "getFluidState", cancellable = true)
	public void getFluidState(BlockState state, CallbackInfoReturnable<FluidState> info)
	{
		info.setReturnValue(FluidProperty.FLUID.getFluidState(state));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void towelette$rebuildStates()
	{
		final BlockState previousDefault = getDefaultState();
		
		final StateFactory.Builder<Block, BlockState> builder = new Builder<Block, BlockState>((Block) (Object) this);
		this.appendProperties(builder);
		if((Object) this instanceof Fluidloggable)
		{
			if(!((StateFactoryBuilderAccessor) builder).getPropertyMap().containsKey(FluidProperty.FLUID.getName()))
			{
				builder.add(FluidProperty.FLUID);
			}
		}
		
		this.stateFactory = builder.build(BlockState::new);
		
		BlockState newDefault = stateFactory.getDefaultState();
		
		for(Entry<Property<?>, Comparable<?>> entry : newDefault.getEntries().entrySet())
		{
			Property property = entry.getKey();
			if(previousDefault.contains(property))
			{
				newDefault = newDefault.with(property, previousDefault.get(property));
			}
		}
		
		this.setDefaultState(newDefault);
	}
}
