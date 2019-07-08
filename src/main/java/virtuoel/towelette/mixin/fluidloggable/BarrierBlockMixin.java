package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BarrierBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(BarrierBlock.class)
public abstract class BarrierBlockMixin implements Fluidloggable
{
	
}
