package virtuoel.towelette.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.hooks.FluidloggableHooks;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Shadow StateFactory<Block, BlockState> stateFactory;
	@Shadow @Final Material material;
	@Shadow abstract BlockState getDefaultState();
	@Shadow abstract void setDefaultState(BlockState state);
	@Shadow void onPlaced(World var1, BlockPos var2, BlockState var3, @Nullable LivingEntity var4, ItemStack var5)
	{}
	@Shadow void onBreak(World var1, BlockPos var2, BlockState var3, PlayerEntity var4)
	{}
	@Shadow BlockState getStateForNeighborUpdate(BlockState var1, Direction var2, BlockState var3, IWorld var4, BlockPos var5, BlockPos var6)
	{ return var1; }
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void onConstruct(Block.Settings var1, CallbackInfo info)
	{
		BlockState defaultState = getDefaultState();
		if(defaultState.contains(FluidProperty.FLUID))
		{
			setDefaultState(defaultState.with(FluidProperty.FLUID, FluidProperty.FLUID.of(Fluids.EMPTY)));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState state, Direction dir, BlockState var3, IWorld world, BlockPos var5, BlockPos var6, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookScheduleFluidTick(state, world, var5);
	}
	
	@Overwrite
	public FluidState getFluidState(BlockState state)
	{
		return FluidProperty.FLUID.getFluidState(state);
	}
	
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	public void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookGetPlacementState((Block) (Object) this, context, info);
	}
	
	@Inject(at = @At("RETURN"), method = "appendProperties", cancellable = true)
	public void onAppendProperties(StateFactory.Builder<Block, BlockState> var1, CallbackInfo info)
	{
		FluidloggableHooks.hookOnAppendProperties((Block) (Object) this, var1, info);
	}
}
