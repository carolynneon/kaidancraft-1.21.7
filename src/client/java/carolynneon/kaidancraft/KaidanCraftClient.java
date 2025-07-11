package carolynneon.kaidancraft;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.network.payload.AP1UpdateFuelPayload;
import carolynneon.kaidancraft.registry.RegisterModelLayers;
import carolynneon.kaidancraft.registry.RegisterRenderers;
import carolynneon.kaidancraft.registry.RegisterScreenHandlerTypes;
import carolynneon.kaidancraft.screen.AP1MenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.world.ClientWorld;

public class KaidanCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RegisterModelLayers.load();
		RegisterRenderers.load();
		//PayloadTypeRegistry.playC2S().register(AP1RenderStatePayload.AP1_RENDER_STATE_ID, AP1RenderStatePayload.CODEC);
		ClientPlayNetworking.registerGlobalReceiver(AP1UpdateFuelPayload.AP1_UPDATE_FUEL_ID, (payload, context) -> {
			ClientWorld world = context.client().world;

			if (world == null) {
				return;
			}

			AP1Entity ap1Entity = (AP1Entity) world.getEntityById(payload.ap1Id());
			if (ap1Entity == null) {
				return;
			}

			ap1Entity.setLitTimeRemaining(payload.litTimeRemaining());
			ap1Entity.setLitTotalTime(payload.litTotalTime());
		});
		//Bind screen to handler
		HandledScreens.register(RegisterScreenHandlerTypes.AP1_MENU, AP1MenuScreen::new);
	}
}