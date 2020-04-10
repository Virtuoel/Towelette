package virtuoel.towelette.mixin.compat114;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(BucketItem.class)
public class BucketItemMixin
{
	@Shadow @Final @Mutable Fluid fluid;
	
	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Material;isSolid()Z"))
	private boolean onPlaceFluidIsSolidProxy(Material obj, @Nullable PlayerEntity playerEntity, World world, BlockPos blockPos, @Nullable BlockHitResult blockHitResult)
	{
		final boolean solid = obj.isSolid();
		final BlockState state = world.getBlockState(blockPos);
		final Block block = ((ToweletteBlockStateExtensions) state).towelette_getBlock();
		
		if (block instanceof FluidFillable)
		{
			return ((FluidFillable) block).canFillWithFluid(world, blockPos, state, fluid) ? solid : true;
		}
		
		return solid;
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Material;isReplaceable()Z"))
	private boolean onPlaceFluidIsReplaceableProxy(Material obj, @Nullable PlayerEntity playerEntity, World world, BlockPos blockPos, @Nullable BlockHitResult blockHitResult)
	{
		final boolean replaceable = obj.isReplaceable();
		final BlockState state = world.getBlockState(blockPos);
		final Block block = ((ToweletteBlockStateExtensions) state).towelette_getBlock();
		
		if (block instanceof FluidFillable)
		{
			return ((FluidFillable) block).canFillWithFluid(world, blockPos, state, fluid) || (world.getFluidState(blockPos).isEmpty() && replaceable);
		}
		
		return replaceable;
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "FIELD", ordinal = 3, target = "Lnet/minecraft/item/BucketItem;fluid:Lnet/minecraft/fluid/Fluid;"))
	private Fluid onPlaceFluidFluidProxy(BucketItem this$0, @Nullable PlayerEntity playerEntity, World world, BlockPos blockPos, @Nullable BlockHitResult blockHitResult)
	{
		if (fluid != Fluids.WATER)
		{
			final BlockState state = world.getBlockState(blockPos);
			return ((FluidFillable) ((ToweletteBlockStateExtensions) state).towelette_getBlock()).canFillWithFluid(world, blockPos, state, fluid) ? Fluids.WATER : fluid;
		}
		
		return Fluids.WATER;
	}
}
