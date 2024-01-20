package virtuoel.towelette.mixin.fluidloggable.compat120plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.SnifferEggBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	DecoratedPotBlock.class,
	SnifferEggBlock.class,
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
