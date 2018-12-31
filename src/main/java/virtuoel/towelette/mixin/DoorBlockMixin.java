package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
		boolean powered = world_1.isReceivingRedstonePower(blockPos_1) || world_1.isReceivingRedstonePower(blockPos_1.offset(blockState_1.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if(!world_1.isClient && powered != blockState_1.get(Properties.POWERED))
		{
			FluidloggableHooks.hookScheduleFluidTick(blockState_1, world_1, blockPos_1);
		}
	}
	
	@Override
	@Overwrite
	public void onBreak(World var1, BlockPos var2, BlockState var3, PlayerEntity var4)
	{
		FluidloggableHooks.hookTallBlockOnBreak((Block) (Object) this, var1, var2, var3, var4);
		super.onBreak(var1, var2, var3, var4);
	}
	
	@Override
	@Overwrite
	public void onPlaced(World var1, BlockPos var2, BlockState var3, LivingEntity var4, ItemStack var5)
	{
		super.onPlaced(var1, var2, var3, var4, var5);
		FluidloggableHooks.hookTallBlockOnPlaced(var1, var2, var3, var4, var5);
	}
}
