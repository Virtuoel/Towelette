package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin
{
	@Inject(at = @At("HEAD"), method = "onUse")
	private void onOnUse(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<Boolean> info)
	{
		if (state.getMaterial() != Material.METAL)
		{
			FluidUtils.scheduleFluidTick(state, world, pos);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "neighborUpdate")
	private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos otherPos, boolean unknown, CallbackInfo info)
	{
		if (!world.isClient && world.isReceivingRedstonePower(pos) != state.get(Properties.POWERED))
		{
			FluidUtils.scheduleFluidTick(state, world, pos);
		}
	}
}
