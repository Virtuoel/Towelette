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
	public void onBreak(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1)
	{
		BedPart bedPart_1 = blockState_1.get(Properties.BED_PART);
		BlockPos blockPos_2 = blockPos_1.offset(blockState_1.get(Properties.FACING_HORIZONTAL), bedPart_1 == BedPart.FOOT ? 1 : -1);
		BlockState blockState_2 = world_1.getBlockState(blockPos_2);
		if(blockState_2.getBlock() == (BedBlock) (Object) this && blockState_2.get(Properties.BED_PART) != bedPart_1)
		{
			world_1.setBlockState(blockPos_2, world_1.getFluidState(blockPos_2).getBlockState(), 35);
			world_1.fireWorldEvent(playerEntity_1, 2001, blockPos_2, Block.getRawIdFromState(blockState_2));
			if(!world_1.isClient && !playerEntity_1.isCreative())
			{
				ItemStack itemStack_1 = playerEntity_1.getMainHandStack();
				Block.dropStacks(blockState_1, world_1, blockPos_1, null, playerEntity_1, itemStack_1);
				Block.dropStacks(blockState_2, world_1, blockPos_2, null, playerEntity_1, itemStack_1);
			}
			
			playerEntity_1.incrementStat(Stats.MINED.method_14956((BedBlock) (Object) this));
		}
		
		super.onBreak(world_1, blockPos_1, blockState_1, playerEntity_1);
	}
	
	@Override
	@Overwrite
	public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, ItemStack itemStack_1)
	{
		super.onPlaced(world_1, blockPos_1, blockState_1, livingEntity_1, itemStack_1);
		if(!world_1.isClient)
		{
			BlockPos blockPos_2 = blockPos_1.offset(blockState_1.get(Properties.FACING_HORIZONTAL));
			world_1.setBlockState(blockPos_2, blockState_1.with(Properties.BED_PART, BedPart.HEAD).with(FluidProperty.FLUID, FluidProperty.FLUID.of(world_1.getFluidState(blockPos_2))), 3);
			world_1.updateNeighbors(blockPos_1, Blocks.AIR);
			blockState_1.updateNeighborStates(world_1, blockPos_1, 3);
		}
	}
}
