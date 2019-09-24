package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(PistonBlockEntity.class)
public abstract class PistonBlockEntityMixin
{
	@Shadow BlockState pushedBlock;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;ZZ)V")
	public void onConstruct(BlockState state, Direction facing, boolean extending, boolean source, CallbackInfo info)
	{
		this.pushedBlock = FluidUtils.getStateWithFluid(state, Fluids.EMPTY.getDefaultState());
	}
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getRenderingState(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	public BlockState tickGetRenderingStateProxy(BlockState blockState_1, IWorld iWorld_1, BlockPos blockPos_1)
	{
		return FluidUtils.getStateWithFluid(Block.getRenderingState(blockState_1, iWorld_1, blockPos_1), Fluids.EMPTY.getDefaultState());
	}
	
	@Redirect(method = { "finish", "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean finish_tick_SetBlockStateProxy(World obj, BlockPos pos, BlockState state, int flags)
	{
		return state == Blocks.AIR.getDefaultState() ? obj.clearBlockState(pos, false) : obj.setBlockState(pos, FluidUtils.getStateWithFluid(state, obj, pos), flags);
	}
}
