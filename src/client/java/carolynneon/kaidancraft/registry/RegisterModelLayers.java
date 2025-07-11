package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.render.entity.model.AP1EntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class RegisterModelLayers {
    public static void register(EntityModelLayer layer, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
    }

    public static void load() {
        register(AP1EntityModel.AP1, AP1EntityModel::getTexturedModelData);
    }
}
