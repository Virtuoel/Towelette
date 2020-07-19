package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import virtuoel.towelette.api.FluidBlockingShapeProvider;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin implements FluidBlockingShapeProvider
{
	@Shadow @Final @Mutable
	static VoxelShape OUTSIDE_SHAPE;
	
	@Override
	public VoxelShape getFluidBlockingShape(BlockState state, BlockView world, BlockPos pos)
	{
		return OUTSIDE_SHAPE;
	}
}
