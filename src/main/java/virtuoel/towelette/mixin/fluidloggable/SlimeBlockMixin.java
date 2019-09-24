package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.SlimeBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(SlimeBlock.class)
public abstract class SlimeBlockMixin implements Fluidloggable
{
	
}
