package virtuoel.towelette.mixin.compat114;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.ReflectionUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(BucketItem.class)
public class BucketItemMixin
{
	@Shadow @Final @Mutable Fluid fluid;
	
	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3614;method_15799()Z", remap = false))
	private boolean onPlaceFluidIsSolidProxy(@Coerce Object material, @Nullable PlayerEntity playerEntity, World world, BlockPos blockPos, @Nullable BlockHitResult blockHitResult)
	{
		final boolean solid = ReflectionUtils.isSolid(material);
		final BlockState state = world.getBlockState(blockPos);
		final Block block = ((ToweletteBlockStateExtensions) state).towelette_getBlock();
		
		if (block instanceof FluidFillable)
		{
			return ReflectionUtils.canFillWithFluid(playerEntity, (FluidFillable) block, world, blockPos, state, fluid) ? solid : true;
		}
		
		return solid;
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_3614;method_15800()Z", remap = false))
	private boolean onPlaceFluidIsReplaceableProxy(@Coerce Object material, @Nullable PlayerEntity playerEntity, World world, BlockPos blockPos, @Nullable BlockHitResult blockHitResult)
	{
		final boolean replaceable = ReflectionUtils.isReplaceable(material);
		final BlockState state = world.getBlockState(blockPos);
		final Block block = ((ToweletteBlockStateExtensions) state).towelette_getBlock();
		
		if (block instanceof FluidFillable)
		{
			return ReflectionUtils.canFillWithFluid(playerEntity, (FluidFillable) block, world, blockPos, state, fluid) || (((ToweletteFluidStateExtensions) (Object) world.getFluidState(blockPos)).towelette_isEmpty() && replaceable);
		}
		
		return replaceable;
	}
	
	@Redirect(method = "placeFluid", at = @At(value = "FIELD", ordinal = 3, target = "Lnet/minecraft/item/BucketItem;fluid:Lnet/minecraft/fluid/Fluid;"))
	private Fluid onPlaceFluidFluidProxy(BucketItem this$0, @Nullable PlayerEntity playerEntity, World world, BlockPos blockPos, @Nullable BlockHitResult blockHitResult)
	{
		final BlockState state = world.getBlockState(blockPos);
		return ReflectionUtils.canFillWithFluid(playerEntity, (FluidFillable) ((ToweletteBlockStateExtensions) state).towelette_getBlock(), world, blockPos, state, fluid) ? Fluids.WATER : Fluids.EMPTY;
	}
}
