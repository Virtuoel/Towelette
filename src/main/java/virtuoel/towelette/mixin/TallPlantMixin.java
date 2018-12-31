package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.hooks.FluidloggableHooks;

@Mixin(TallPlantBlock.class)
public abstract class TallPlantMixin extends BlockMixin
{
	@Override
	@Overwrite
	public void onBreak(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1)
	{
		FluidloggableHooks.hookTallBlockOnBreak((Block) (Object) this, world_1, blockPos_1, blockState_1, playerEntity_1);
		super.onBreak(world_1, blockPos_1, blockState_1, playerEntity_1);
	}
	
	@Override
	@Overwrite
	public void onPlaced(World var1, BlockPos var2, BlockState var3, LivingEntity var4, ItemStack var5)
	{
		super.onPlaced(var1, var2, var3, var4, var5);
		FluidloggableHooks.hookTallBlockOnPlaced(var1, var2, var3, var4, var5);
	}
}
