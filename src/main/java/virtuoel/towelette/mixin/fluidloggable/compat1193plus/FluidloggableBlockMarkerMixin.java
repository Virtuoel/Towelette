package virtuoel.towelette.mixin.fluidloggable.compat1193plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.WallHangingSignBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	HangingSignBlock.class,
	WallHangingSignBlock.class
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
