package virtuoel.towelette.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.google.gson.JsonElement;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.api.ToweletteConfig;
import virtuoel.towelette.util.FluidUtils;

@Mixin(PistonBlockEntity.class)
public abstract class PistonBlockEntityMixin extends BlockEntity
{
	private PistonBlockEntityMixin()
	{
		super(null, null, null);
	}
	
	@ModifyArg(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private BlockState finishSetBlockStateProxy(BlockPos pos, BlockState state, int flags)
	{
		final boolean unpushableFluids = Optional.ofNullable(ToweletteConfig.DATA.get("unpushableFluids"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsBoolean).orElse(true);
		
		return state == Blocks.AIR.getDefaultState() ? this.world.getFluidState(pos).getBlockState() : !unpushableFluids ? state : FluidUtils.getStateWithFluid(state, this.world, pos);
	}
}
