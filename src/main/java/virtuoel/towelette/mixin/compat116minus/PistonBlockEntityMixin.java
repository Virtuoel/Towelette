package virtuoel.towelette.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;
import virtuoel.towelette.util.ToweletteFluidStateExtensions;

@Mixin(PistonBlockEntity.class)
public abstract class PistonBlockEntityMixin extends BlockEntity
{
	@Shadow(remap = false) BlockState field_12204;
	@Shadow(remap = false) boolean field_12202;
	
	private PistonBlockEntityMixin()
	{
		super(null, null, null);
	}
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/class_2680;Lnet/minecraft/class_2350;ZZ)V", remap = false)
	private void onConstruct(BlockState state, Direction facing, boolean extending, boolean source, CallbackInfo info)
	{
		final boolean unpushableFluids = ToweletteConfig.COMMON.unpushableFluids.get();
		
		if (unpushableFluids)
		{
			this.field_12204 = FluidUtils.getStateWithFluid(state, Fluids.EMPTY.getDefaultState());
		}
	}
	
	@Redirect(method = "method_16896", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2248;method_9510(Lnet/minecraft/class_2680;Lnet/minecraft/class_1936;Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;", remap = false), remap = false)
	private BlockState tickPostProcessStateProxy(BlockState blockState, WorldAccess world, BlockPos blockPos)
	{
		return FluidUtils.getStateWithFluid(Block.postProcessState(blockState, world, blockPos), Fluids.EMPTY.getDefaultState());
	}
	
	@ModifyArg(method = "method_16896", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8652(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)Z", remap = false), remap = false)
	private BlockState tickSetBlockStateProxy(BlockPos pos, BlockState state, int flags)
	{
		/*
		final boolean unpushableFluids = ToweletteConfig.COMMON.unpushableFluids.get();
		
		if (unpushableFluids && state == Blocks.AIR.getDefaultState())
		{
			return ((ToweletteFluidStateExtensions) (Object) this.world.getFluidState(pos)).towelette_getBlockState();
		}
		else if (unpushableFluids || field_12202)
		{
			return FluidUtils.getStateWithFluid(state, this.world, pos);
		}
		*/
		
		return state;
	}
}
