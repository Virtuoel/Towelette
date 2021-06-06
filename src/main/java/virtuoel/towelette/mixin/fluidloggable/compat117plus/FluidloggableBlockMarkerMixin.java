package virtuoel.towelette.mixin.fluidloggable.compat117plus;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BigDripleafBlock;
import net.minecraft.block.BigDripleafStemBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.CaveVinesBodyBlock;
import net.minecraft.block.CaveVinesHeadBlock;
import net.minecraft.block.GlowLichenBlock;
import net.minecraft.block.HangingRootsBlock;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.block.SmallDripleafBlock;
import net.minecraft.block.SporeBlossomBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	AzaleaBlock.class,
	AmethystClusterBlock.class,
	BigDripleafBlock.class,
	BigDripleafStemBlock.class,
	CandleBlock.class,
	CandleCakeBlock.class,
	CaveVinesBodyBlock.class,
	CaveVinesHeadBlock.class,
	GlowLichenBlock.class,
	HangingRootsBlock.class,
	PointedDripstoneBlock.class,
	SculkSensorBlock.class,
	SmallDripleafBlock.class,
	SporeBlossomBlock.class,
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
