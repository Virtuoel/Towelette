package virtuoel.towelette.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.VoxelShape;

@Mixin(VoxelShape.class)
public interface VoxelShapeAccessor
{
	@Accessor("voxels")
	VoxelSet towelette_getVoxels();
}
