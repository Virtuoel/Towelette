package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.CoralDeadWallFanBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.RedstoneTorchWallBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StandingBannerBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.TripwireBlock;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.ItemPlacementContext;
import virtuoel.towelette.hooks.FluidloggableHooks;

@Mixin({
	BambooBlock.class,
	StandingBannerBlock.class,
	WallBannerBlock.class,
	SkullBlock.class,
	WallSkullBlock.class,
	BellBlock.class,
	ChestBlock.class,
	ConduitBlock.class,
	EnderChestBlock.class,
	HopperBlock.class,
	LecternBlock.class,
	StandingSignBlock.class,
	WallSignBlock.class,
	ChorusPlantBlock.class,
	CoralDeadWallFanBlock.class,
	DoorBlock.class,
	EndPortalFrameBlock.class,
	EndRodBlock.class,
	AnvilBlock.class,
	FenceBlock.class,
	PaneBlock.class,
	WallBlock.class,
	AbstractRedstoneGateBlock.class,
	BedBlock.class,
	CocoaBlock.class,
	FenceGateBlock.class,
	TrapdoorBlock.class,
	WallMountedBlock.class,
	LadderBlock.class,
	LanternBlock.class,
//	SeagrassBlock.class,
	SeaPickleBlock.class,
	TallPlantBlock.class,
//	TallSeagrassBlock.class,
//	RedstoneWireBlock.class,
	ScaffoldingBlock.class,
	SlabBlock.class,
	SnowBlock.class,
	StairsBlock.class,
	RedstoneTorchWallBlock.class,
	WallTorchBlock.class,
	TripwireBlock.class,
	TripwireHookBlock.class,
	TurtleEggBlock.class,
	VineBlock.class,
})
public abstract class FluidloggableBlockPlacementMixin
{
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		FluidloggableHooks.hookGetPlacementState((Block) (Object) this, context, info);
	}
}
