package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.FarmlandBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin implements Fluidloggable
{
	
}
