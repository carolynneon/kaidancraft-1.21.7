package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.network.payload.AP1UpdateFuelPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class RegisterPayloadTypes {
    public static void load() {
        //PayloadTypeRegistry.playC2S().register(EntityIDPayload.ENTITY_ID_PAYLOAD_ID, EntityIDPayload.CODEC);
        //PayloadTypeRegistry.playS2C().register(OpenAP1ScreenPayload.OPEN_AP1_SCREEN_ID, OpenAP1ScreenPayload.CODEC);
        //PayloadTypeRegistry.playC2S().register(AP1RenderStatePayload.AP1_RENDER_STATE_ID, AP1RenderStatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AP1UpdateFuelPayload.AP1_UPDATE_FUEL_ID, AP1UpdateFuelPayload.CODEC);
    }
}
