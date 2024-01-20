package virtuoel.towelette.mixin.fluidloggable.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.DecoratedPotBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	DecoratedPotBlock.class,
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
