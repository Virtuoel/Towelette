package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.GrassPathBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(GrassPathBlock.class)
public abstract class GrassPathBlockMixin implements Fluidloggable
{
	
}
