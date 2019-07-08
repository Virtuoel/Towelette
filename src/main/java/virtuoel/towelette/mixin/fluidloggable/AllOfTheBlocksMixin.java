package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.Block;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(Block.class)
public abstract class AllOfTheBlocksMixin implements Fluidloggable
{
	
}
