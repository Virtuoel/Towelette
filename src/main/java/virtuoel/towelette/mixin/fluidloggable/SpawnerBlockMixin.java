package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.SpawnerBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin implements Fluidloggable
{
	
}
