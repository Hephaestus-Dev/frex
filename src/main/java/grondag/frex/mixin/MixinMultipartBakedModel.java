package grondag.frex.mixin;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.MultipartBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(MultipartBakedModel.class)
public abstract class MixinMultipartBakedModel implements FabricBakedModel {
    @Shadow @Final private Map<BlockState, BitSet> stateCache;

    @Shadow @Final private List<Pair<Predicate<BlockState>, BakedModel>> components;

    @Override
    public boolean isVanillaAdapter() {
        return true;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> randomSupplier, RenderContext renderContext) {
        if (blockState != null) {
            BitSet bitSet = this.stateCache.get(blockState);
            if (bitSet == null) {
                bitSet = new BitSet();

                for(int i = 0; i < this.components.size(); ++i) {
                    Pair<Predicate<BlockState>, BakedModel> pair = this.components.get(i);
                    if (pair.getLeft().test(blockState)) {
                        bitSet.set(i);
                    }
                }

                this.stateCache.put(blockState, bitSet);
            }

            for(int j = 0; j < bitSet.length(); ++j) {
                if (bitSet.get(j)) {
                    ((FabricBakedModel)(this.components.get(j)).getRight()).emitBlockQuads(blockRenderView, blockState, blockPos, randomSupplier, renderContext);
                }
            }
        }
    }
}
