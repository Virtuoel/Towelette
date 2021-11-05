package virtuoel.towelette.util;

import net.minecraft.util.math.BlockPos;

public interface ToweletteTickSchedulerExtensions<T>
{
	void towelette_schedule(BlockPos pos, T object, int rate);
}
