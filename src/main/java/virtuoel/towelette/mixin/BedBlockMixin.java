package virtuoel.towelette.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.api.FluidProperty;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin extends BlockMixin
{
	@Override
	@Overwrite
	public void onBreak(World var1, BlockPos var2, BlockState var3, PlayerEntity var4)
	{
		BedPart var5 = var3.get(Properties.BED_PART);
		BlockPos var6 = var2.offset(var3.get(Properties.FACING_HORIZONTAL), var5 == BedPart.FOOT ? 1 : -1);
		BlockState var7 = var1.getBlockState(var6);
		if(var7.getBlock() == (BedBlock) (Object) this && var7.get(Properties.BED_PART) != var5)
		{
			var1.setBlockState(var6, var1.getFluidState(var6).getBlockState(), 35);
			var1.fireWorldEvent(var4, 2001, var6, Block.getRawIdFromState(var7));
			if(!var1.isClient && !var4.isCreative())
			{
				ItemStack var8 = var4.getMainHandStack();
				Block.dropStacks(var3, var1, var2, null, var4, var8);
				Block.dropStacks(var7, var1, var6, null, var4, var8);
			}
			
			var4.incrementStat(Stats.MINED.method_14956((BedBlock) (Object) this));
		}
		
		super.onBreak(var1, var2, var3, var4);
	}
	
	@Override
	@Overwrite
	public void onPlaced(World var1, BlockPos var2, BlockState var3, @Nullable LivingEntity var4, ItemStack var5)
	{
		super.onPlaced(var1, var2, var3, var4, var5);
		if(!var1.isClient)
		{
			BlockPos var6 = var2.offset(var3.get(Properties.FACING_HORIZONTAL));
			var1.setBlockState(var6, var3.with(Properties.BED_PART, BedPart.HEAD).with(FluidProperty.FLUID, FluidProperty.FLUID.of(var1.getFluidState(var6))), 3);
			var1.updateNeighbors(var2, Blocks.AIR);
			var3.updateNeighborStates(var1, var2, 3);
		}
	}
}
