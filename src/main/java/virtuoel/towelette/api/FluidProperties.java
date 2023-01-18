package virtuoel.towelette.api;

import net.minecraft.fluid.Fluid;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import virtuoel.statement.api.property.RegistryEntryProperty;
import virtuoel.towelette.util.RegistryUtils;

public class FluidProperties
{
	public static final RegistryEntryProperty<Fluid> FLUID = new RegistryEntryProperty<>("fluid", RegistryUtils.FLUID_REGISTRY);
	public static final IntProperty LEVEL_1_8 = IntProperty.of("fluid_level", 1, 8);
	public static final BooleanProperty FALLING = BooleanProperty.of("falling_fluid");
}
