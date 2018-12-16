package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.RedstoneWireBlock;
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
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import virtuoel.towelette.api.FluidProperty;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
//	PressurePlateBlock.class,
//	WeightedPressurePlateBlock.class,
	AnvilBlock.class,
	BambooBlock.class,
//	BrewingStandBlock.class,
//	CakeBlock.class,
//	CarpetBlock.class,
	EndRodBlock.class,
	DoorBlock.class,
//	DragonEggBlock.class,
//	FlowerPotBlock.class,
	FenceGateBlock.class,
	BedBlock.class,
//	RailBlock.class,
//	DetectorRailBlock.class,
//	PoweredRailBlock.class,
//	RedstoneTorchBlock.class,
	WallTorchBlock.class,
	RedstoneWireBlock.class,
	AbstractRedstoneGateBlock.class,
	StandingBannerBlock.class,
	WallBannerBlock.class,
	SkullBlock.class,
	WallSkullBlock.class,
//	WallMountedBlock.class, // Special placement
	CocoaBlock.class,
	LanternBlock.class,
	LecternBlock.class,
	BellBlock.class,
//	StonecutterBlock.class,
	CoralParentBlock.class,
	TurtleEggBlock.class,
//	PlantBlock.class,
	SnowBlock.class,
	HopperBlock.class,
	
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
public abstract class FluidloggableBlockPlacementMixin
{
	@Inject(at = @At("RETURN"), method = "getPlacementState", cancellable = true)
	private void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info)
	{
		BlockState state = info.getReturnValue();
		if(state != null)
		{
			if(state.contains(Properties.WATERLOGGED))
			{
				state = state.with(Properties.WATERLOGGED, false);
			}
			
			if(state.contains(FluidProperty.FLUID))
			{
				FluidState fluid = context.getWorld().getFluidState(context.getPos());
				info.setReturnValue(state.with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluid)));
			}
		}
	}
}
