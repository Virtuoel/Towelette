package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ReflectionUtils;
import virtuoel.towelette.util.ToweletteBlockStateExtensions;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin
{
	@Inject(at = @At("HEAD"), method = "neighborUpdate")
	private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos otherPos, boolean unknown, CallbackInfo info)
	{
		if (!world.isClient && ReflectionUtils.isReceivingRedstonePower(world, pos) != ((ToweletteBlockStateExtensions) state).<Boolean>towelette_get(Properties.POWERED))
		{
			FluidUtils.scheduleFluidTick(state, world, pos);
		}
	}
}
