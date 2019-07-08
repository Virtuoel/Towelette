package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BeaconBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(BeaconBlock.class)
public abstract class BeaconBlockMixin implements Fluidloggable
{
	
}
