package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import virtuoel.towelette.hooks.FluidloggableHooks;
import virtuoel.towelette.util.FluidUtils;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends BlockMixin
{
	@Inject(at = @At("HEAD"), method = "activate", cancellable = true)
	public void onActivate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1, CallbackInfoReturnable<Boolean> info)
	{
		if(material != Material.METAL)
		{
			FluidloggableHooks.hookScheduleFluidTick(blockState_1, world_1, blockPos_1);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "neighborUpdate", cancellable = true)
	public void onNeighborUpdate(BlockState blockState_1, World world_1, BlockPos blockPos_1, Block block_1, BlockPos blockPos_2, CallbackInfo info)
	{
		boolean powered = world_1.isReceivingRedstonePower(blockPos_1) || world_1.isReceivingRedstonePower(blockPos_1.offset(blockState_1.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if(!world_1.isClient && powered != blockState_1.get(Properties.POWERED))
		{
			FluidloggableHooks.hookScheduleFluidTick(blockState_1, world_1, blockPos_1);
		}
	}
	
	@Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean onBreakSetBlockStateProxy(World obj, BlockPos blockPos_1, BlockState blockState_1, int int_1)
	{
		return obj.setBlockState(blockPos_1, obj.getFluidState(blockPos_1).getBlockState(), int_1);
	}
	
	@Redirect(method = "onPlaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean onPlacedSetBlockStateProxy(World obj, BlockPos blockPos_1, BlockState blockState_1, int int_1)
	{
		return obj.setBlockState(blockPos_1, FluidUtils.getStateWithFluid(blockState_1, obj, blockPos_1), int_1);
	}
}
