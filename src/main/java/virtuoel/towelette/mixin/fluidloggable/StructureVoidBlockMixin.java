package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.StructureVoidBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(StructureVoidBlock.class)
public abstract class StructureVoidBlockMixin implements Fluidloggable
{
	
}
