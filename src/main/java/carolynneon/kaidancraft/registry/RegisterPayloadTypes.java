package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.network.payload.AP1RenderStatePayload;
import carolynneon.kaidancraft.network.payload.EntityIDPayload;
import carolynneon.kaidancraft.network.payload.OpenAP1ScreenPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import javax.swing.text.html.parser.Entity;

public class RegisterPayloadTypes {
    public static void load() {
        //PayloadTypeRegistry.playC2S().register(EntityIDPayload.ENTITY_ID_PAYLOAD_ID, EntityIDPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OpenAP1ScreenPayload.OPEN_AP1_SCREEN_ID, OpenAP1ScreenPayload.CODEC);
        //PayloadTypeRegistry.playC2S().register(AP1RenderStatePayload.AP1_RENDER_STATE_ID, AP1RenderStatePayload.CODEC);
    }
}
