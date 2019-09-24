package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.RedstoneBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(RedstoneBlock.class)
public abstract class RedstoneBlockMixin implements Fluidloggable
{
	
}
