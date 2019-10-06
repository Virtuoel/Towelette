package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.towelette.util.FluidUtils;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity
{
	@Shadow BlockState block;
	
	private FallingBlockEntityMixin(EntityType<?> entityType, World world)
	{
		super(entityType, world);
	}
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/block/BlockState;)V")
	private void onConstruct(World world, double x, double y, double z, BlockState state, CallbackInfo info)
	{
		this.block = FluidUtils.getStateWithFluid(state, Fluids.EMPTY.getDefaultState());
	}
	
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private BlockState tickSetBlockStateProxy(BlockPos pos, BlockState state, int flags)
	{
		return this.block = FluidUtils.getStateWithFluid(state, this.world, pos);
	}
}
