package virtuoel.towelette.mixin.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin
{
	@Inject(at = @At("HEAD"), method = "onUse")
	private void onOnUse(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> info)
	{
		if (((ToweletteBlockStateExtensions) state).towelette_getMaterial() != Material.METAL)
		{
			FluidUtils.scheduleFluidTick(state, world, pos);
		}
	}
}
