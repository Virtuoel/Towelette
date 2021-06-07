package virtuoel.towelette.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import virtuoel.towelette.util.ToweletteEntityExtensions;

@Mixin(Entity.class)
public abstract class EntityMixin implements ToweletteEntityExtensions
{
	@Shadow(remap = false) abstract void method_20447();
	
	@Override
	public void towelette_setInLava()
	{
		method_20447();
	}
}
