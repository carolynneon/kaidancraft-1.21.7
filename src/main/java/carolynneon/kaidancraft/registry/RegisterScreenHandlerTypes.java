package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.screenhandler.AP1MenuScreenHandler;
import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.network.payload.EntityIDPayload;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class RegisterScreenHandlerTypes {
    public static final ScreenHandlerType<AP1MenuScreenHandler> AP1_MENU = register("ap1_menu", AP1MenuScreenHandler::new, EntityIDPayload.CODEC);

    public static <T extends ScreenHandler, S extends CustomPayload> ExtendedScreenHandlerType<T, S>
    register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, S> factory,
             PacketCodec<? super RegistryByteBuf, S> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, KaidanCraft.id(name), new ExtendedScreenHandlerType<>(factory, codec));
    }

    public static void load() {

    }
}
