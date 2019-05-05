package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
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
import net.minecraft.block.ConnectedPlantBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FernBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.HorizontalConnectedBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.SeaPickleBlock;
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
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
	AbstractPressurePlateBlock.class,
	AbstractRailBlock.class,
	BambooBlock.class,
	BambooSaplingBlock.class,
	AbstractBannerBlock.class,
	AbstractSkullBlock.class,
	BellBlock.class,
	BrewingStandBlock.class,
	CampfireBlock.class,
	ChestBlock.class,
	ComposterBlock.class,
	ConduitBlock.class,
	DaylightDetectorBlock.class,
	EnchantingTableBlock.class,
	EnderChestBlock.class,
	HopperBlock.class,
	LecternBlock.class,
	SignBlock.class,
	WallSignBlock.class,
	CactusBlock.class,
	CakeBlock.class,
	CarpetBlock.class,
//	CauldronBlock.class,
	ChorusFlowerBlock.class,
	ConnectedPlantBlock.class,
	CoralParentBlock.class,
	DoorBlock.class,
	EndPortalFrameBlock.class,
	EndRodBlock.class,
	AnvilBlock.class,
	DragonEggBlock.class,
	FlowerPotBlock.class,
	HorizontalConnectedBlock.class,
	AbstractRedstoneGateBlock.class,
	BedBlock.class,
	CocoaBlock.class,
	FenceGateBlock.class,
	TrapdoorBlock.class,
	WallMountedBlock.class,
	LadderBlock.class,
	LanternBlock.class,
	CropBlock.class,
	BeetrootsBlock.class,
	NetherWartBlock.class,
	DeadBushBlock.class,
	FernBlock.class,
	FlowerBlock.class,
	MushroomPlantBlock.class,
	SaplingBlock.class,
	SeaPickleBlock.class,
	AttachedStemBlock.class,
	StemBlock.class,
	SweetBerryBushBlock.class,
	TallPlantBlock.class,
	RedstoneWireBlock.class,
	ScaffoldingBlock.class,
	SlabBlock.class,
	SnowBlock.class,
	StairsBlock.class,
	StonecutterBlock.class,
	SugarCaneBlock.class,
	TorchBlock.class,
	TripwireBlock.class,
	TripwireHookBlock.class,
	TurtleEggBlock.class,
	VineBlock.class,
	CobwebBlock.class,
})
public abstract class FluidloggableMixin implements Fluidloggable
{
	
}
