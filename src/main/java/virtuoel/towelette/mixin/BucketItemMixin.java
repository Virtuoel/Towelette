package virtuoel.towelette.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(BucketItem.class)
public class BucketItemMixin
{
	@Shadow Fluid fluid;
	
	@Redirect(method = "use", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public BlockState onUseGetBlockStateProxy(World obj, BlockPos blockPos_1)
	{
		final BlockState original = obj.getBlockState(blockPos_1);
		final FluidState state = FluidUtils.getFluidState(original);
		if(!state.isEmpty() && !state.isStill())
		{
			return Blocks.AIR.getDefaultState();
		}
		return original;
	}
	
	@Redirect(method = "use", at = @At(value = "FIELD", ordinal = 2, target = "Lnet/minecraft/item/BucketItem;fluid:Lnet/minecraft/fluid/Fluid;"))
	public Fluid onUseFluidProxy(BucketItem this$0, World world_1, PlayerEntity playerEntity_1, Hand hand_1)
	{
		return Fluids.EMPTY;
	}
	
	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/math/BlockPos;"))
	public BlockPos onUseOffsetProxy(BlockPos obj, Direction side, World world_1, PlayerEntity playerEntity_1, Hand hand_1)
	{
		if(world_1.getFluidState(obj).getFluid() != fluid)
		{
			final BlockState state = world_1.getBlockState(obj);
			final Block block = state.getBlock();
			return block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(world_1, obj, state, fluid) ? obj : obj.offset(side);
		}
		return obj.offset(side);
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Material;isSolid()Z"))
	public boolean onPlaceFluidIsSolidProxy(Material obj, @Nullable PlayerEntity playerEntity_1, World world_1, BlockPos blockPos_1, @Nullable BlockHitResult blockHitResult_1)
	{
		final boolean solid = obj.isSolid();
		final BlockState state = world_1.getBlockState(blockPos_1);
		final Block block = state.getBlock();
		if(block instanceof FluidFillable)
		{
			return ((FluidFillable) block).canFillWithFluid(world_1, blockPos_1, state, fluid) ? solid : true;
		}
		return solid;
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Material;isReplaceable()Z"))
	public boolean onPlaceFluidIsReplaceableProxy(Material obj, @Nullable PlayerEntity playerEntity_1, World world_1, BlockPos blockPos_1, @Nullable BlockHitResult blockHitResult_1)
	{
		final boolean replaceable = obj.isReplaceable();
		final BlockState state = world_1.getBlockState(blockPos_1);
		final Block block = state.getBlock();
		if(block instanceof FluidFillable)
		{
			return ((FluidFillable) block).canFillWithFluid(world_1, blockPos_1, state, fluid) || (world_1.getFluidState(blockPos_1).isEmpty() && replaceable);
		}
		return replaceable;
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "FIELD", ordinal = 3, target = "Lnet/minecraft/item/BucketItem;fluid:Lnet/minecraft/fluid/Fluid;"))
	public Fluid onPlaceFluidFluidProxy(BucketItem this$0, @Nullable PlayerEntity playerEntity_1, World world_1, BlockPos blockPos_1, @Nullable BlockHitResult blockHitResult_1)
	{
		if(fluid != Fluids.WATER)
		{
			final BlockState state = world_1.getBlockState(blockPos_1);
			return ((FluidFillable) state.getBlock()).canFillWithFluid(world_1, blockPos_1, state, fluid) ? Fluids.WATER : fluid;
		}
		return Fluids.WATER;
	}
}
