package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
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
import net.minecraft.block.ChestBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EnchantingTableBlock;
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
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StandingBannerBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.state.StateFactory;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
	PressurePlateBlock.class,
	WeightedPressurePlateBlock.class,
	AnvilBlock.class,
	BambooBlock.class,
	BrewingStandBlock.class,
	CakeBlock.class,
//	CarpetBlock.class,
	EndRodBlock.class,
	DoorBlock.class,
//	DragonEggBlock.class,
//	FlowerPotBlock.class,
	FenceGateBlock.class,
	BedBlock.class,
	RailBlock.class,
	DetectorRailBlock.class,
	PoweredRailBlock.class,
	RedstoneTorchBlock.class,
	WallTorchBlock.class,
	RedstoneWireBlock.class,
	ComparatorBlock.class,
	RepeaterBlock.class,
	StandingBannerBlock.class,
	WallBannerBlock.class,
	SkullBlock.class,
	WallSkullBlock.class,
	AbstractButtonBlock.class,
	GrindstoneBlock.class,
	LeverBlock.class,
	CocoaBlock.class,
	LanternBlock.class,
	LecternBlock.class,
	BellBlock.class,
//	StonecutterBlock.class,
	CoralParentBlock.class,
	TurtleEggBlock.class,
//	PlantBlock.class,
	SnowBlock.class,
	CactusBlock.class,
	HopperBlock.class,
	EnchantingTableBlock.class,
	
	ChestBlock.class,
	ConduitBlock.class,
	CoralParentBlock.class,
	EnderChestBlock.class,
	FenceBlock.class,
	PaneBlock.class,
	WallBlock.class,
	LadderBlock.class,
	ScaffoldingBlock.class,
	SeaPickleBlock.class,
	StandingSignBlock.class,
	WallSignBlock.class,
	SlabBlock.class,
	StairsBlock.class,
	TrapdoorBlock.class
})
@Implements(@Interface(iface = Fluidloggable.class, prefix = "fluidloggable$"))
public abstract class FluidloggableBlockPropertyMixin
{
	@Inject(at = @At("RETURN"), method = "appendProperties(Lnet/minecraft/state/StateFactory$Builder;)V", cancellable = true)
	private void onAppendProperties(StateFactory.Builder<Block, BlockState> var1, CallbackInfo info)
	{
		FluidProperty.FLUID.tryAppendPropertySafely(var1);
	}
}
