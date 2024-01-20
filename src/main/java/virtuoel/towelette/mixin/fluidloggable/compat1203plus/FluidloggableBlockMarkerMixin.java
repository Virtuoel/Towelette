package virtuoel.towelette.mixin.fluidloggable.compat1203plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.GrateBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	GrateBlock.class,
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
