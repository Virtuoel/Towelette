package virtuoel.towelette.mixin.fluidloggable.compat116plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.FungusBlock;
import net.minecraft.block.RootsBlock;
import net.minecraft.block.SproutsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.block.WeepingVinesPlantBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
	FungusBlock.class,
	RootsBlock.class,
	SproutsBlock.class,
	WallBlock.class,
	WeepingVinesBlock.class,
	WeepingVinesPlantBlock.class,
})
public abstract class FluidloggableMixin implements Fluidloggable
{
	
}
