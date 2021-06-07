package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(targets = "net.minecraft.class_3610", remap = false)
public interface FluidStateMixin extends ToweletteFluidStateExtensions
{
	@Shadow(remap = false) Fluid method_15772();
	@Shadow(remap = false) boolean method_15769();
	@Shadow(remap = false) boolean method_15771();
	@Shadow(remap = false) VoxelShape method_17776(BlockView world, BlockPos pos);
	@Shadow(remap = false) BlockState method_15759();
	
	@Override
	default Fluid towelette_getFluid()
	{
		return method_15772();
	}
	
	@Override
	default boolean towelette_isEmpty()
	{
		return method_15769();
	}
	
	@Override
	default boolean towelette_isStill()
	{
		return method_15771();
	}
	
	@Override
	default VoxelShape towelette_getShape(BlockView world, BlockPos pos)
	{
		return method_17776(world, pos);
	}
	
	@Override
	default BlockState towelette_getBlockState()
	{
		return method_15759();
	}
}
