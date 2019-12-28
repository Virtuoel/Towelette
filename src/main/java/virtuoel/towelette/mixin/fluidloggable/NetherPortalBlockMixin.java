package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.NetherPortalBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin implements Fluidloggable
{
	
}
