package grondag.frex.mixin;

import net.minecraft.client.render.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"net/minecraft/client/render/model/WeightedBakedModel$Entry"})
public interface WeightedBakedModelEntryAccessor {
    @Accessor
    BakedModel getModel();
}
