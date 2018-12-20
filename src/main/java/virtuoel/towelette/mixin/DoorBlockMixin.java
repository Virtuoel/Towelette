package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.api.FluidProperty;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends BlockMixin
{
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
