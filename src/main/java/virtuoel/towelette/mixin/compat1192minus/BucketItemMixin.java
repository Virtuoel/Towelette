package virtuoel.towelette.mixin.compat1192minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(BucketItem.class)
public class BucketItemMixin
{
	@Shadow Fluid fluid;
	
	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/math/BlockPos;"))
	private BlockPos onUseOffsetProxy(BlockPos obj, Direction side, World world, PlayerEntity playerEntity, Hand hand)
	{
		if (((ToweletteFluidStateExtensions) (Object) world.getFluidState(obj)).towelette_getFluid() != fluid)
		{
			final BlockState state = world.getBlockState(obj);
			final Block block = ((ToweletteBlockStateExtensions) state).towelette_getBlock();
			return block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(world, obj, state, fluid) ? obj : obj.offset(side);
		}
		
		return obj.offset(side);
	}
}
