package virtuoel.towelette.api;

import net.minecraft.fluid.Fluid;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.registry.Registry;

public class FluidProperties
{
	public static final RegistryEntryProperty<Fluid> FLUID = new RegistryEntryProperty<>("fluid", Registry.FLUID);
	public static final IntProperty LEVEL_1_8 = IntProperty.of("fluid_level", 1, 8);
	public static final BooleanProperty FALLING = BooleanProperty.of("falling_fluid");
}
