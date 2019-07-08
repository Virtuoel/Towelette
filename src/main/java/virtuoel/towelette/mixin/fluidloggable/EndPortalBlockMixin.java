package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.EndPortalBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(EndPortalBlock.class)
public abstract class EndPortalBlockMixin implements Fluidloggable
{
	
}
