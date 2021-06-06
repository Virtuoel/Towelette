package virtuoel.towelette.mixin.compat117plus.towelette;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Fluidloggable.class)
public interface FluidloggableMixin
{
	default ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state)
	{
		final Item item = FluidUtils.tryDrainFluid(world, pos, state).getBucketItem();
		return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
	}
}
