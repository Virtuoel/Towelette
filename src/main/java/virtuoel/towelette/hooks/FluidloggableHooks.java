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
				FluidState fluid = context.getWorld().getFluidState(context.getPos());
				if(state.contains(Properties.WATERLOGGED) && fluid.getFluid() != Fluids.WATER)
				{
					state = state.with(Properties.WATERLOGGED, false);
				}
				state = state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluid));
			}
			
			info.setReturnValue(state);
		}
	}
	
	public static void hookOnAppendProperties(Block self, StateFactory.Builder<Block, BlockState> var1, CallbackInfo info)
	{
		if(self instanceof Fluidloggable)
		{
			FluidProperty.FLUID.tryAppendPropertySafely(var1);
		}
	}
	
	public static void hookScheduleFluidTick(BlockState state, IWorld world, BlockPos pos)
	{
		Fluid fluid = FluidProperty.FLUID.getFluid(state);
		if(fluid != Fluids.EMPTY)
		{
			world.getFluidTickScheduler().schedule(pos, fluid, fluid.method_15789(world));
		}
	}
	
	public static void hookTallBlockOnBreak(Block self, World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1)
	{
		DoubleBlockHalf var5 = blockState_1.get(Properties.DOUBLE_BLOCK_HALF);
		BlockPos var6 = var5 == DoubleBlockHalf.LOWER ? blockPos_1.up() : blockPos_1.down();
		BlockState var7 = world_1.getBlockState(var6);
		if(var7.getBlock() ==  self && var7.get(Properties.DOUBLE_BLOCK_HALF) != var5)
		{
			world_1.setBlockState(var6, world_1.getFluidState(var6).getBlockState(), 35);
			world_1.fireWorldEvent(playerEntity_1, 2001, var6, Block.getRawIdFromState(var7));
			if(!world_1.isClient && !playerEntity_1.isCreative())
			{
				ItemStack var8 = playerEntity_1.getMainHandStack();
				Block.dropStacks(blockState_1, world_1, blockPos_1, null, playerEntity_1, var8);
				Block.dropStacks(var7, world_1, var6, null, playerEntity_1, var8);
			}
		}
	}
	
	public static void hookTallBlockOnPlaced(World var1, BlockPos var2, BlockState var3, LivingEntity var4, ItemStack var5)
	{
		if(!var1.isClient)
		{
			var1.setBlockState(var2.up(), var3.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).with(FluidProperty.FLUID, FluidProperty.FLUID.of(var1.getFluidState(var2.up()))), 3);
			var1.updateNeighbors(var2, Blocks.AIR);
			var3.updateNeighborStates(var1, var2, 3);
		}
	}
}
