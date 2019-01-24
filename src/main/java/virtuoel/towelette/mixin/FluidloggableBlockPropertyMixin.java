package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.RedstoneTorchWallBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StandingBannerBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.TripwireBlock;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.state.StateFactory;

@Mixin({
	PressurePlateBlock.class,
	WeightedPressurePlateBlock.class,
	DetectorRailBlock.class,
	PoweredRailBlock.class,
	RailBlock.class,
	BambooBlock.class,
	StandingBannerBlock.class,
	WallBannerBlock.class,
	SkullBlock.class,
	WallSkullBlock.class,
	BellBlock.class,
	BrewingStandBlock.class,
	CampfireBlock.class,
	ChestBlock.class,
	ConduitBlock.class,
	DaylightDetectorBlock.class,
	EnderChestBlock.class,
	HopperBlock.class,
	LecternBlock.class,
	StandingSignBlock.class,
	WallSignBlock.class,
	CactusBlock.class,
	CakeBlock.class,
//	CauldronBlock.class,
	ChorusFlowerBlock.class,
	ChorusPlantBlock.class,
	CoralParentBlock.class,
	DeadCoralWallFanBlock.class,
	DoorBlock.class,
	EndPortalFrameBlock.class,
	EndRodBlock.class,
	AnvilBlock.class,
	FenceBlock.class,
	PaneBlock.class,
	WallBlock.class,
	ComparatorBlock.class,
	RepeaterBlock.class,
	BedBlock.class,
	CocoaBlock.class,
	FenceGateBlock.class,
	TrapdoorBlock.class,
	AbstractButtonBlock.class,
	GrindstoneBlock.class,
	LeverBlock.class,
	LadderBlock.class,
	LanternBlock.class,
//	CropBlock.class,
//	BeetrootsBlock.class,
//	NetherWartBlock.class,
//	SaplingBlock.class,
	SeaPickleBlock.class,
//	StemAttachedBlock.class,
//	StemBlock.class,
	SweetBerryBushBlock.class,
	TallPlantBlock.class,
//	RedstoneWireBlock.class,
	ScaffoldingBlock.class,
	SlabBlock.class,
	SnowBlock.class,
	StairsBlock.class,
	SugarCaneBlock.class,
	RedstoneTorchBlock.class,
	RedstoneTorchWallBlock.class,
	WallTorchBlock.class,
	TripwireBlock.class,
	TripwireHookBlock.class,
	TurtleEggBlock.class,
	VineBlock.class,
})
public abstract class FluidloggableBlockPropertyMixin extends BlockMixin
{
	@Override
	@Inject(at = @At("RETURN"), method = "appendProperties(Lnet/minecraft/state/StateFactory$Builder;)V", cancellable = true)
	public void onAppendProperties(StateFactory.Builder<Block, BlockState> builder, CallbackInfo info)
	{
		super.onAppendProperties(builder, info);
	}
}
