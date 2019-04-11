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

package grondag.frex.api;

import java.util.function.BooleanSupplier;

import grondag.frex.api.material.MaterialFinder;
import grondag.frex.api.material.Pipeline;
import grondag.frex.api.material.PipelineBuilder;
import grondag.frex.api.material.RenderCondition;
import grondag.frex.api.material.RenderMaterial;
import grondag.frex.api.mesh.MeshBuilder;
import net.minecraft.util.Identifier;

/**
 * Interface for rendering plug-ins that provide enhanced capabilities
 * for model lighting, buffering and rendering. Such plug-ins implement the
 * enhanced model rendering interfaces specified by the Fabric API.<p>
 */
public interface Renderer {
    /**
     * Obtain a new {@link MeshBuilder} instance used to create 
     * baked models with enhanced features.<p>
     * 
     * Renderer does not retain a reference to returned instances and they should be re-used for 
     * multiple models when possible to avoid memory allocation overhead.
     */
    MeshBuilder meshBuilder();
    
    /**
     * Obtain a new {@link MaterialFinder} instance used to retrieve 
     * standard {@link RenderMaterial} instances.<p>
     * 
     * Renderer does not retain a reference to returned instances and they should be re-used for 
     * multiple materials when possible to avoid memory allocation overhead.
     */
    MaterialFinder materialFinder();

    /**
     * Return a material previously registered via {@link #registerMaterial(Identifier, RenderMaterial)}.
     * Will return null if no material was found matching the given identifier.
     */
    RenderMaterial materialById(Identifier id);
    
    /**
     * Register a material for re-use by other mods or models within a mod.
     * The registry does not persist registrations - mods must create and register 
     * all materials at game initialization.<p>
     * 
     * Returns false if a material with the given identifier is already present,
     * leaving the existing material intact.
     */
    boolean registerMaterial(Identifier id, RenderMaterial material);
    
    PipelineBuilder pipelineBuilder();
    
    Pipeline pipelineById(Identifier id);
    
    boolean registerPipeline(Identifier id, Pipeline pipeline);
    
    RenderCondition createCondition(BooleanSupplier supplier, boolean affectBlocks, boolean affectItems);
    
    RenderCondition conditionById(Identifier id);
    
    boolean registerCondition(Identifier id, RenderCondition pipeline);
}