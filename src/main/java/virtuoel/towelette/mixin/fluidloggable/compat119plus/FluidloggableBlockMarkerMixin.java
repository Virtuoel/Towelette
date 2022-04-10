package virtuoel.towelette.mixin.fluidloggable.compat119plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MangroveRootsBlock;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.block.SculkVeinBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	LeavesBlock.class,
	MangroveRootsBlock.class,
	SculkVeinBlock.class,
	SculkShriekerBlock.class
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
