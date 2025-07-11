package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.entity.projectile.BoltEntity;
import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class RegisterEntityTypes {

    public static final EntityType<BoltEntity> BOLT = register("bolt", EntityType.Builder.<BoltEntity>create(BoltEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F)
            .maxTrackingRange(4)
            .trackingTickInterval(10));
    public static final EntityType<AP1Entity> AP1 = register("ap1", EntityType.Builder.<AP1Entity>create(AP1Entity::new, SpawnGroup.MISC)
            .dropsNothing()
            .makeFireImmune()
            .dimensions(2.275F, 2.375F)
            .eyeHeight(2.375F)
            .maxTrackingRange(10));

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
        // Create the entity type key.
        RegistryKey<EntityType<?>> entityTypeKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, KaidanCraft.id(name));

        return Registry.register(
            Registries.ENTITY_TYPE,
            KaidanCraft.id(name),
            type.build(entityTypeKey)
        );
    }

    public static void load() {}
}
