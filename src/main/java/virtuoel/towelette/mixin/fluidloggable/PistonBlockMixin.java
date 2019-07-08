package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.PistonHeadBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
	PistonBlock.class,
	PistonExtensionBlock.class,
	PistonHeadBlock.class,
})
public abstract class PistonBlockMixin implements Fluidloggable
{
	
}
