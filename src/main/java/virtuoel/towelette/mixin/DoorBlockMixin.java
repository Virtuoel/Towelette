package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.hooks.FluidloggableHooks;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends BlockMixin
{
	@Inject(at = @At("HEAD"), method = "activate", cancellable = true)
	public void onActivate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, Direction direction_1, float float_1, float float_2, float float_3, CallbackInfoReturnable<Boolean> info)
	{
		if(material != Material.METAL)
		{
			FluidloggableHooks.hookScheduleFluidTick(blockState_1, world_1, blockPos_1);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "neighborUpdate", cancellable = true)
	public void onNeighborUpdate(BlockState blockState_1, World world_1, BlockPos blockPos_1, Block block_1, BlockPos blockPos_2, CallbackInfo info)
	{
		boolean powered = world_1.isReceivingRedstonePower(blockPos_1) || world_1.isReceivingRedstonePower(blockPos_1.offset(blockState_1.get(Properties.DOOR_HALF) == BlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if(!world_1.isClient && powered != blockState_1.get(Properties.POWERED))
		{
			FluidloggableHooks.hookScheduleFluidTick(blockState_1, world_1, blockPos_1);
		}
	}
	
	@Override
	@Overwrite
	public void onBreak(World var1, BlockPos var2, BlockState var3, PlayerEntity var4)
	{
		BlockHalf var5 = var3.get(Properties.DOOR_HALF);
		BlockPos var6 = var5 == BlockHalf.LOWER ? var2.up() : var2.down();
		BlockState var7 = var1.getBlockState(var6);
		if(var7.getBlock() ==  (DoorBlock) (Object) this && var7.get(Properties.DOOR_HALF) != var5)
		{
			var1.setBlockState(var6, var1.getFluidState(var6).getBlockState(), 35);
			var1.fireWorldEvent(var4, 2001, var6, Block.getRawIdFromState(var7));
			if(!var1.isClient && !var4.isCreative())
			{
				ItemStack var8 = var4.getMainHandStack();
				Block.dropStacks(var3, var1, var2, null, var4, var8);
				Block.dropStacks(var7, var1, var6, null, var4, var8);
			}
		}
		
		super.onBreak(var1, var2, var3, var4);
	}
	
	@Override
	@Overwrite
	public void onPlaced(World var1, BlockPos var2, BlockState var3, LivingEntity var4, ItemStack var5)
	{
		super.onPlaced(var1, var2, var3, var4, var5);
		if(!var1.isClient)
		{
			var1.setBlockState(var2.up(), var3.with(Properties.DOOR_HALF, BlockHalf.UPPER).with(FluidProperty.FLUID, FluidProperty.FLUID.of(var1.getFluidState(var2.up()))), 3);
			var1.updateNeighbors(var2, Blocks.AIR);
			var3.updateNeighborStates(var1, var2, 3);
		}
	}
}
