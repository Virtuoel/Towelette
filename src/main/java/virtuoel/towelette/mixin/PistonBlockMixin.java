package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
{
	@Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean moveSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		final boolean unpushableFluids = ToweletteConfig.COMMON.unpushableFluids.get();
		
		return unpushableFluids && state == Blocks.AIR.getDefaultState() ? obj.removeBlock(pos, false) : obj.setBlockState(pos, !unpushableFluids ? state : FluidUtils.getStateWithFluid(state, obj, pos), flags);
	}
	
	@Redirect(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private boolean onSyncedBlockEventSetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return state == Blocks.AIR.getDefaultState() ? obj.removeBlock(pos, false) : obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos), flags);
	}
}
