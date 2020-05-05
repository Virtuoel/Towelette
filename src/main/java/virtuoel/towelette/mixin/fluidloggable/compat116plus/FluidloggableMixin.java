package virtuoel.towelette.mixin.fluidloggable.compat116plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.ChainBlock;
import net.minecraft.block.FungusBlock;
import net.minecraft.block.RootsBlock;
import net.minecraft.block.SproutsBlock;
import net.minecraft.block.TwistingVinesBlock;
import net.minecraft.block.TwistingVinesPlantBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.block.WeepingVinesPlantBlock;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
	ChainBlock.class,
	FungusBlock.class,
	RootsBlock.class,
	SproutsBlock.class,
	TwistingVinesBlock.class,
	TwistingVinesPlantBlock.class,
	WallBlock.class,
	WeepingVinesBlock.class,
	WeepingVinesPlantBlock.class,
})
public abstract class FluidloggableMixin implements Fluidloggable
{
	
}
