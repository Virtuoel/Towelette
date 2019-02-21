package virtuoel.towelette.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
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
	@Shadow void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, ItemStack itemStack_1)
	{}
	@Shadow void onBreak(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1)
	{}
	@Shadow BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2)
	{ return blockState_1; }
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void onConstruct(Block.Settings block$Settings_1, CallbackInfo info)
	{
		BlockState defaultState = getDefaultState();
		if(defaultState.contains(FluidProperty.FLUID))
		{
			setDefaultState(defaultState.with(FluidProperty.FLUID, FluidProperty.FLUID.of(Fluids.EMPTY)));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookScheduleFluidTick(blockState_1, iWorld_1, blockPos_1);
	}
	
	@Inject(at = @At("HEAD"), method = "getFluidState", cancellable = true)
	public void getFluidState(BlockState state, CallbackInfoReturnable<FluidState> info)
	{
		info.setReturnValue(FluidProperty.FLUID.getFluidState(state));
	}
	
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	public void onGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookGetPlacementState((Block) (Object) this, context, info);
	}
	
	@Inject(at = @At("RETURN"), method = "appendProperties", cancellable = true)
	public void onAppendProperties(StateFactory.Builder<Block, BlockState> builder, CallbackInfo info)
	{
		FluidloggableHooks.hookOnAppendProperties((Block) (Object) this, builder, info);
	}
}
