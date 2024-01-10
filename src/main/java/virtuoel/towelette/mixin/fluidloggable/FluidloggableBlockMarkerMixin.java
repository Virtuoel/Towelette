package virtuoel.towelette.mixin.fluidloggable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooShootBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BeetrootsBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RodBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.ShortPlantBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.TripwireBlock;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.WallSignBlock;
import virtuoel.towelette.util.AutomaticFluidloggableMarker;
import virtuoel.towelette.util.AutomaticWaterloggableMarker;

@Mixin({
	AbstractPressurePlateBlock.class,
	AbstractRailBlock.class,
	BambooBlock.class,
	BambooShootBlock.class,
	AbstractBannerBlock.class,
	ChestBlock.class,
	EnderChestBlock.class,
	SignBlock.class,
	WallSignBlock.class,
	AbstractSkullBlock.class,
	BellBlock.class,
	BrewingStandBlock.class,
	CampfireBlock.class,
	ConduitBlock.class,
	DaylightDetectorBlock.class,
	EnchantingTableBlock.class,
	HopperBlock.class,
	LecternBlock.class,
	CactusBlock.class,
	CakeBlock.class,
	CarpetBlock.class,
	ChorusFlowerBlock.class,
	CobwebBlock.class,
	ComposterBlock.class,
	ConnectingBlock.class,
	CoralParentBlock.class,
	DoorBlock.class,
	EndPortalFrameBlock.class,
	RodBlock.class,
	AnvilBlock.class,
	DragonEggBlock.class,
	FlowerPotBlock.class,
	HorizontalConnectingBlock.class,
	AbstractRedstoneGateBlock.class,
	BedBlock.class,
	CocoaBlock.class,
	FenceGateBlock.class,
	TrapdoorBlock.class,
	WallMountedBlock.class,
	LadderBlock.class,
	LanternBlock.class,
	AttachedStemBlock.class,
	CropBlock.class,
	BeetrootsBlock.class,
	DeadBushBlock.class,
	ShortPlantBlock.class,
	FlowerBlock.class,
	MushroomPlantBlock.class,
	NetherWartBlock.class,
	SaplingBlock.class,
	SeaPickleBlock.class,
	StemBlock.class,
	SweetBerryBushBlock.class,
	TallPlantBlock.class,
	ScaffoldingBlock.class,
	SlabBlock.class,
	SnowBlock.class,
	StairsBlock.class,
	StonecutterBlock.class,
	SugarCaneBlock.class,
	TripwireBlock.class,
	TripwireHookBlock.class,
	TurtleEggBlock.class,
	VineBlock.class,
//	PistonBlock.class,
//	PistonExtensionBlock.class,
//	PistonHeadBlock.class,
	RedstoneWireBlock.class,
	TorchBlock.class,
})
public abstract class FluidloggableBlockMarkerMixin implements AutomaticFluidloggableMarker, AutomaticWaterloggableMarker
{
	
}
