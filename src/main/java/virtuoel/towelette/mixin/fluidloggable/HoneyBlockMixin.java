package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.HoneyBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(HoneyBlock.class)
public abstract class HoneyBlockMixin implements Fluidloggable
{
	
}
