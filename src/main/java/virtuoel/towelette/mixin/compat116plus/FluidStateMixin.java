package virtuoel.towelette.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(FluidState.class)
public abstract class FluidStateMixin implements ToweletteFluidStateExtensions
{
	@Shadow abstract Fluid getFluid();
	@Shadow abstract boolean isEmpty();
	@Shadow abstract boolean isStill();
	@Shadow abstract VoxelShape getShape(BlockView world, BlockPos pos);
	@Shadow abstract BlockState getBlockState();
	@Shadow abstract float getHeight(BlockView world, BlockPos pos);
	
	@Override
	public Fluid towelette_getFluid()
	{
		return getFluid();
	}
	
	@Override
	public boolean towelette_isEmpty()
	{
		return isEmpty();
	}
	
	@Override
	public boolean towelette_isStill()
	{
		return isStill();
	}
	
	@Override
	public VoxelShape towelette_getShape(BlockView world, BlockPos pos)
	{
		return getShape(world, pos);
	}
	
	@Override
	public BlockState towelette_getBlockState()
	{
		return getBlockState();
	}
	
	@Override
	public float towelette_getHeight(BlockView world, BlockPos pos)
	{
		return getHeight(world, pos);
	}
}
