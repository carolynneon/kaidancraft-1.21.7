package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.entity.projectile.BoltEntity;
import carolynneon.kaidancraft.render.entity.renderer.AP1EntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class RegisterRenderers {

    public static <T extends Entity, S extends EntityRenderState> void register(EntityType<T> type, EntityRendererFactory<T> entityRendererFactory) {
        EntityRendererRegistry.register(type, entityRendererFactory);
    }

    public static void load() {
        register(RegisterEntityTypes.BOLT, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterEntityTypes.AP1, AP1EntityRenderer::new);
    }
}
