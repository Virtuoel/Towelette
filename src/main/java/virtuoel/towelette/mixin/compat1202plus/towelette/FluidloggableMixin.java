package virtuoel.towelette.mixin.compat1202plus.towelette;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.api.Fluidloggable;
import virtuoel.towelette.util.FluidUtils;

@Mixin(Fluidloggable.class)
public interface FluidloggableMixin
{
	default boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return method_10310(player, world, pos, state, fluid);
	}
	
	default boolean method_10310(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return FluidUtils.canFillWithFluid(world, pos, state, fluid);
	}
	
	default ItemStack tryDrainFluid(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state)
	{
		return method_9700(player, world, pos, state);
	}
	
	default ItemStack method_9700(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state)
	{
		final Item item = FluidUtils.tryDrainFluid(world, pos, state).getBucketItem();
		return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
	}
}
