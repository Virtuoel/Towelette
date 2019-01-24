package virtuoel.towelette.hooks;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;

public class FluidloggableHooks
{
	public static void hookGetPlacementState(Block self, ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		BlockState state = info.getReturnValue();
		if(state != null)
		{
			if(state.contains(FluidProperty.FLUID))
			{
				FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
				if(state.contains(Properties.WATERLOGGED) && fluid.getFluid() != Fluids.WATER)
				{
					state = state.with(Properties.WATERLOGGED, false);
				}
				state = state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluid));
			}
			
			info.setReturnValue(state);
		}
	}
	
	public static void hookOnAppendProperties(Block self, StateFactory.Builder<Block, BlockState> builder, CallbackInfo info)
	{
		if(self instanceof Fluidloggable)
		{
			FluidProperty.FLUID.tryAppendPropertySafely(builder);
		}
	}
	
	public static void hookScheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		Fluid fluid = FluidProperty.FLUID.getFluid(state);
		if(fluid != Fluids.EMPTY)
		{
			world.getFluidTickScheduler().schedule(pos, fluid, fluid.getTickRate(world));
		}
	}
	
	public static void hookTallBlockOnBreak(Block self, World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1)
	{
		DoubleBlockHalf doubleBlockHalf_1 = (DoubleBlockHalf) blockState_1.get(Properties.DOUBLE_BLOCK_HALF);
		BlockPos blockPos_2 = doubleBlockHalf_1 == DoubleBlockHalf.LOWER ? blockPos_1.up() : blockPos_1.down();
		BlockState blockState_2 = world_1.getBlockState(blockPos_2);
		if(blockState_2.getBlock() == self && blockState_2.get(Properties.DOUBLE_BLOCK_HALF) != doubleBlockHalf_1)
		{
			world_1.setBlockState(blockPos_2, world_1.getFluidState(blockPos_2).getBlockState(), 35);
			world_1.fireWorldEvent(playerEntity_1, 2001, blockPos_2, Block.getRawIdFromState(blockState_2));
			if(!world_1.isClient && !playerEntity_1.isCreative())
			{
				Block.dropStacks(blockState_1, world_1, blockPos_1, null, playerEntity_1, playerEntity_1.getMainHandStack());
				Block.dropStacks(blockState_2, world_1, blockPos_2, null, playerEntity_1, playerEntity_1.getMainHandStack());
			}
		}
	}
	
	public static void hookTallBlockOnPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1)
	{
		if(!world_1.isClient)
		{
			world_1.setBlockState(blockPos_1.up(), blockState_1.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).with(FluidProperty.FLUID, FluidProperty.FLUID.of(world_1.getFluidState(blockPos_1.up()))), 3);
			world_1.updateNeighbors(blockPos_1, Blocks.AIR);
			blockState_1.updateNeighborStates(world_1, blockPos_1, 3);
		}
	}
}
