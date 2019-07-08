package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.LilyPadBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(LilyPadBlock.class)
public abstract class LilyPadBlockMixin implements Fluidloggable
{
	
}
