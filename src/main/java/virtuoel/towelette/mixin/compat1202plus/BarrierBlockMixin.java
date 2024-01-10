package virtuoel.towelette.mixin.compat1202plus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BarrierBlock.class)
public class BarrierBlockMixin
{
	@Redirect(method = "canFillWithFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Waterloggable;canFillWithFluid(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/fluid/Fluid;)Z"))
	private boolean canFillWithFluidProxy(Waterloggable obj, @Nullable PlayerEntity playerEntity, BlockView world, BlockPos blockPos, BlockState blockState, Fluid fluid)
	{
		return FluidUtils.canFillWithFluid(world, blockPos, blockState, fluid);
	}
	
	@Redirect(method = "tryDrainFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Waterloggable;tryDrainFluid(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"))
	private ItemStack tryDrainFluidProxy(Waterloggable obj, @Nullable PlayerEntity playerEntity, WorldAccess world, BlockPos blockPos, BlockState blockState)
	{
		final Item item = FluidUtils.tryDrainFluid(world, blockPos, blockState).getBucketItem();
		return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
	}
}
