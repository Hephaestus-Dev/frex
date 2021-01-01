package grondag.frex.mixin;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.WeightedBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mixin(WeightedBakedModel.class)
public class MixinWeightedBakedModel implements FabricBakedModel {
    @Shadow @Final private List<WeightedPicker.Entry> models;

    @Shadow @Final private int totalWeight;

    @Override
    public boolean isVanillaAdapter() {
        return true;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> randomSupplier, RenderContext renderContext) {
        Random random = randomSupplier.get();
        BakedModel model = ((WeightedBakedModelEntryAccessor) WeightedPicker.getAt(this.models, Math.abs((int)random.nextLong()) % this.totalWeight)).getModel();
        ((FabricBakedModel) model).emitBlockQuads(blockRenderView, blockState, blockPos, randomSupplier, renderContext);
    }

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> randomSupplier, RenderContext renderContext) {
        Random random = randomSupplier.get();
        BakedModel model = ((WeightedBakedModelEntryAccessor) WeightedPicker.getAt(this.models, Math.abs((int)random.nextLong()) % this.totalWeight)).getModel();
        ((FabricBakedModel) model).emitItemQuads(itemStack, randomSupplier, renderContext);
    }
}
