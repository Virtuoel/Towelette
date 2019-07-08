package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.LeavesBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin implements Fluidloggable
{
	
}
