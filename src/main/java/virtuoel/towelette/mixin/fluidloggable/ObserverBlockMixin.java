package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.ObserverBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(ObserverBlock.class)
public abstract class ObserverBlockMixin implements Fluidloggable
{
	
}
