package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.CauldronBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin implements Fluidloggable
{
	
}
