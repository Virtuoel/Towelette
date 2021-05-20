package virtuoel.towelette.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonElement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;

@Mixin(PistonBlockEntity.class)
public abstract class PistonBlockEntityMixin extends BlockEntity
{
	@Shadow BlockState pushedBlock;
	@Shadow boolean source;
	
	private PistonBlockEntityMixin()
	{
		super(null, null, null);
	}
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;ZZ)V")
	private void onConstruct(BlockPos pos, BlockState state, BlockState pushedBlock, Direction facing, boolean extending, boolean source, CallbackInfo info)
	{
		final boolean unpushableFluids = Optional.ofNullable(ToweletteConfig.DATA.get("unpushableFluids"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(true);
		
		if (unpushableFluids)
		{
			this.pushedBlock = FluidUtils.getStateWithFluid(pushedBlock, Fluids.EMPTY.getDefaultState());
		}
	}
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;postProcessState(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
	private static BlockState tickPostProcessStateProxy(BlockState blockState, WorldAccess world, BlockPos blockPos)
	{
		return FluidUtils.getStateWithFluid(Block.postProcessState(blockState, world, blockPos), Fluids.EMPTY.getDefaultState());
	}
	
	@ModifyArg(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private BlockState finishSetBlockStateProxy(BlockPos pos, BlockState state, int flags)
	{
		final boolean unpushableFluids = Optional.ofNullable(ToweletteConfig.DATA.get("unpushableFluids"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(true);
		
		return state == Blocks.AIR.getDefaultState() ? this.world.getFluidState(pos).getBlockState() : !unpushableFluids ? state : FluidUtils.getStateWithFluid(state, this.world, pos);
	}
	
	@Unique private static BlockState pushed = null;
	
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private static void onTick(World world, BlockPos pos, BlockState state, PistonBlockEntity blockEntity, CallbackInfo info)
	{
		final boolean unpushableFluids = Optional.ofNullable(ToweletteConfig.DATA.get("unpushableFluids"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(true);
		
		if (unpushableFluids && state == Blocks.AIR.getDefaultState())
		{
			pushed = world.getFluidState(pos).getBlockState();
		}
		else if (unpushableFluids || blockEntity.isSource())
		{
			pushed = FluidUtils.getStateWithFluid(state, world, pos);
		}
	}
	
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private static BlockState tickSetBlockStateProxy(BlockPos pos, BlockState state, int flags)
	{
		return pushed == null ? state : pushed;
	}
}
