package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.RedstoneWireBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin implements Fluidloggable
{
	
}
