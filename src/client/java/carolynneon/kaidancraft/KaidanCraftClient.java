package carolynneon.kaidancraft;

import carolynneon.kaidancraft.network.payload.AP1RenderStatePayload;
import carolynneon.kaidancraft.registry.RegisterModelLayers;
import carolynneon.kaidancraft.registry.RegisterRenderers;
import carolynneon.kaidancraft.registry.RegisterScreenHandlerTypes;
import carolynneon.kaidancraft.screen.AP1MenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.network.packet.PlayPackets;

public class KaidanCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RegisterModelLayers.load();
		RegisterRenderers.load();
		//PayloadTypeRegistry.playC2S().register(AP1RenderStatePayload.AP1_RENDER_STATE_ID, AP1RenderStatePayload.CODEC);

		//Bind screen to handler
		HandledScreens.register(RegisterScreenHandlerTypes.AP1_MENU, AP1MenuScreen::new);
	}
}