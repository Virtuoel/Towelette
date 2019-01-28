package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CoralTubeFanBlock;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StandingBannerBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.TripwireBlock;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

@Mixin({
	AbstractPressurePlateBlock.class,
	BambooBlock.class,
	BambooSaplingBlock.class,
	StandingBannerBlock.class,
	WallBannerBlock.class,
	BellBlock.class,
	CampfireBlock.class,
	ChestBlock.class,
	StandingSignBlock.class,
	WallSignBlock.class,
	CakeBlock.class,
	CarpetBlock.class,
	ChorusPlantBlock.class,
	DeadCoralWallFanBlock.class,
	CoralWallFanBlock.class,
	CoralTubeFanBlock.class,
	DoorBlock.class,
	FlowerPotBlock.class,
	FenceBlock.class,
	PaneBlock.class,
	WallBlock.class,
	RepeaterBlock.class,
	BedBlock.class,
	CocoaBlock.class,
	FenceGateBlock.class,
	TrapdoorBlock.class,
	WallMountedBlock.class,
	LadderBlock.class,
	LanternBlock.class,
	PlantBlock.class,
	SeaPickleBlock.class,
	AttachedStemBlock.class,
	TallPlantBlock.class,
	RedstoneWireBlock.class,
	ScaffoldingBlock.class,
	SlabBlock.class,
	SnowBlock.class,
	StairsBlock.class,
	SugarCaneBlock.class,
	TorchBlock.class,
	WallTorchBlock.class,
	TripwireBlock.class,
	TripwireHookBlock.class,
	VineBlock.class,
	
//	ConduitBlock.class,
//	EnderChestBlock.class,
//	SignBlock.class,
//	CactusBlock.class,
//	ChorusFlowerBlock.class,
//	CoralBlockBlock.class,
//	CoralBlock.class,
//	GrassPathBlock.class,
//	SeagrassBlock.class,
//	RedstoneTorchBlock.class,
})
public abstract class FluidloggableNeighborUpdateMixin extends BlockMixin
{
	@Override
	@Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate", cancellable = true)
	public void onGetStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2, CallbackInfoReturnable<BlockState> info)
	{
		super.onGetStateForNeighborUpdate(blockState_1, direction_1, blockState_2, iWorld_1, blockPos_1, blockPos_2, info);
	}
}
