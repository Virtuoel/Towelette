package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;
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
			if(!((StateFactoryBuilderAccessor) builder).getPropertyMap().containsKey(FluidProperty.FLUID.getName()))
			{
				builder.with(FluidProperty.FLUID);
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
}
