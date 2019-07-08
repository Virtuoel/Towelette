package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.PortalBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(PortalBlock.class)
public abstract class PortalBlockMixin implements Fluidloggable
{
	
}
