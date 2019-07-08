package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.FireBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin implements Fluidloggable
{
	
}
