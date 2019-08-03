package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;

@Mixin(DefaultedRegistry.class)
public abstract class DefaultedRegistryMixin
{
	@Shadow @Final Identifier defaultId;
	
	@ModifyArg(method = "set", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/SimpleRegistry;set(ILnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;"))
	private Identifier setDefault(Identifier identifier_1)
	{
		return defaultId.equals(identifier_1) ? defaultId : identifier_1;
	}
}
