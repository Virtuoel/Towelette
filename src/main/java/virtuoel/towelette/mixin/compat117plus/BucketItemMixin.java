package virtuoel.towelette.mixin.compat117plus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item
{
	@Shadow @Final @Mutable Fluid fluid;
	
	private BucketItemMixin()
	{
		super(null);
	}
	
	@Unique Optional<SoundEvent> sound = Optional.empty();
	
	@Inject(method = "use", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/block/FluidDrainable;tryDrainFluid(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"))
	private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info, ItemStack itemStack, BlockHitResult blockHitResult, BlockPos blockPos, BlockPos blockPos2, BlockState blockState, FluidDrainable fluidDrainable, World world2)
	{
		final FluidState looking = world.getFluidState(blockPos);
		sound = looking.getFluid().getBucketFillSound();
	}
	
	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FluidDrainable;getBucketFillSound()Ljava/util/Optional;"))
	private Optional<SoundEvent> useGetBucketFillSoundProxy(FluidDrainable obj)
	{
		final Optional<SoundEvent> s = sound;
		sound = Optional.empty();
		return s.isPresent() ? s : obj.getBucketFillSound();
	}
}
