package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin
{
	@Shadow abstract FluidState getFluidState();
	@Shadow @Final @Mutable int luminance;
	
	@Unique boolean setFluidLuminance = false;
	
	@Inject(at = @At("HEAD"), method = "getLuminance")
	private void onGetLuminance(CallbackInfoReturnable<Integer> info)
	{
		if (!this.setFluidLuminance)
		{
			this.setFluidLuminance = true;
			
			final FluidState fluidState = getFluidState();
			
			if (fluidState.getFluid() != Fluids.EMPTY)
			{
				final BlockState fluidBlockState = fluidState.getBlockState();
				
				if (fluidBlockState != (BlockState) (Object) this)
				{
					final int fluidLuminance = fluidBlockState.getLuminance();
					
					if (fluidLuminance > this.luminance)
					{
						this.luminance = fluidLuminance;
					}
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getStateForNeighborUpdate")
	private void onGetStateForNeighborUpdate(Direction direction, BlockState blockState, IWorld world, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, pos);
	}
	
	@Inject(at = @At("RETURN"), method = "onBlockAdded")
	private void onOnBlockAdded(World world, BlockPos blockPos, BlockState blockState, boolean flag, CallbackInfo info)
	{
		FluidUtils.scheduleFluidTick((BlockState) (Object) this, world, blockPos);
	}
}
