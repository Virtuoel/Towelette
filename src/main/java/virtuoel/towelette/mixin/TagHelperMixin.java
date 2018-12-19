package virtuoel.towelette.mixin;

import java.util.Iterator;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.TagHelper;
import net.minecraft.util.registry.Registry;

@Mixin(TagHelper.class)
public class TagHelperMixin
{
	@Shadow static final Logger LOGGER = LogManager.getLogger();
	
	@Overwrite
	public static BlockState deserializeBlockState(CompoundTag compound)
	{
		if(!compound.containsKey("Name", 8))
		{
			return Blocks.AIR.getDefaultState();
		}
		else
		{
			Block block = Registry.BLOCK.get(new Identifier(compound.getString("Name")));
			BlockState state = block.getDefaultState();
			if(compound.containsKey("Properties", 10))
			{
				CompoundTag properties = compound.getCompound("Properties");
				StateFactory<Block, BlockState> stateFactory = block.getStateFactory();
				Iterator<String> iter = properties.getKeys().iterator();
				
				while(iter.hasNext())
				{
					String key = iter.next();
					Property<?> property = stateFactory.getProperty(key);
					if(property != null)
					{
						try
						{
							state = withProperty(state, property, key, properties, compound);
						}
						catch (IllegalArgumentException e)
						{
							LOGGER.warn(e.getMessage());
						}
					}
				}
			}
			
			return state;
		}
	}
	
	@Shadow
	private static <S extends PropertyContainer<S>, T extends Comparable<T>> S withProperty(S container, Property<T> property, String key, CompoundTag properties, CompoundTag compound)
	{
		Optional<T> value = property.getValue(properties.getString(key));
		if(value.isPresent())
		{
			return container.with(property, value.get());
		}
		else
		{
			LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", key, properties.getString(key), compound.toString());
			return container;
		}
	}
}
