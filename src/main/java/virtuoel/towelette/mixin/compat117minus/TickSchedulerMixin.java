package virtuoel.towelette.mixin.compat117minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.BlockPos;
import virtuoel.towelette.util.ToweletteTickSchedulerExtensions;

@Mixin(targets = "net.minecraft.class_1951", remap = false)
public interface TickSchedulerMixin<T> extends ToweletteTickSchedulerExtensions<T>
{
	@Shadow(remap = false)
	void method_8676(BlockPos pos, T object, int delay);
	
	@Override
	default void towelette_schedule(BlockPos pos, T object, int rate)
	{
		method_8676(pos, object, rate);
	}
}
