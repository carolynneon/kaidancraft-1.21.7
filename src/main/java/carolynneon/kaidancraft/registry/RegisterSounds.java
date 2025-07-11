package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.KaidanCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class RegisterSounds {
    public static final SoundEvent ENTITY_AP1_ENGINE = registerSoundEvent("entity_ap1_engine");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = KaidanCraft.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void load() {

    }
}
