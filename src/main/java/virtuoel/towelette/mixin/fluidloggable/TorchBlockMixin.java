package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.TorchBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin implements Fluidloggable
{
	
}
