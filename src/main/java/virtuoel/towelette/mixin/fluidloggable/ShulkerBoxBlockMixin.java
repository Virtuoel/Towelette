package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.ShulkerBoxBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin implements Fluidloggable
{
	
}
