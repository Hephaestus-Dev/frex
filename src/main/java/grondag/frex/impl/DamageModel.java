/*******************************************************************************
 * Copyright 2019 grondag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package grondag.frex.impl;

import java.util.Random;
import java.util.function.Supplier;

import grondag.frex.api.core.FabricBakedModel;
import grondag.frex.api.core.ForwardingBakedModel;
import grondag.frex.api.core.MutableQuadView;
import grondag.frex.api.core.RenderContext;
import grondag.frex.api.core.RenderContext.QuadTransform;
import grondag.frex.api.core.RenderMaterial;
import grondag.frex.api.core.RendererAccess;
import grondag.frex.api.core.TerrainBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;

/**
 * Specialized model wrapper that implements a general-purpose 
 * block-breaking render for enhanced models.<p>
 * 
 * Works by intercepting all model output and redirecting to dynamic
 * quads that are baked with single-layer, UV-locked damage texture.
 */
public class DamageModel extends ForwardingBakedModel {
    static final RenderMaterial DAMAGE_MATERIAL = RendererAccess.INSTANCE.hasRenderer() ? RendererAccess.INSTANCE.getRenderer().materialFinder().find() : null;
    
    private DamageTransform damageTransform = new DamageTransform();
    
    public void prepare(BakedModel wrappedModel, Sprite sprite, BlockState blockState, BlockPos blockPos) {
        this.damageTransform.damageSprite = sprite;
        this.wrapped = wrappedModel;
    }
    
    @Override
    public void emitBlockQuads(TerrainBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.pushTransform(damageTransform);
        ((FabricBakedModel)wrapped).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }
    
    private static class DamageTransform implements QuadTransform {
        private Sprite damageSprite;
        
        @Override
        public boolean transform(MutableQuadView quad) {
            quad.material(DAMAGE_MATERIAL);
            quad.spriteBake(0, damageSprite, MutableQuadView.BAKE_LOCK_UV);
            quad.colorIndex(-1);
            return true;
        }
    }
}