package carolynneon.kaidancraft;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.network.payload.AP1RenderStatePayload;
import carolynneon.kaidancraft.registry.*;
import carolynneon.kaidancraft.registry.worldgen.RegisterBiomeModifications;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KaidanCraft implements ModInitializer {
	public static final String MOD_ID = "kaidancraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading...");
		//RegisterBiomeModifications.load();
		//RegisterBlocks.load();
		RegisterEntityTypes.load();
		RegisterItemGroups.load();
		RegisterItems.load();
		//RegisterPayloadTypes.load();
		RegisterScreenHandlerTypes.load();
		RegisterSounds.load();
//		ServerPlayNetworking.registerGlobalReceiver(AP1RenderStatePayload.AP1_RENDER_STATE_ID, (payload, context) -> {
//			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
//			if (entity instanceof AP1Entity ap1Entity) {
//				ap1Entity.setMudFlapsMoving(payload.mudFlapsMoving());
//				ap1Entity.setSittingRight(payload.sittingRight());
//				ap1Entity.setSittingLeft(payload.sittingLeft());
//				ap1Entity.setLeverRight(payload.leverRight());
//				ap1Entity.setLeverLeft(payload.leverLeft());
//			}
//		});
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}