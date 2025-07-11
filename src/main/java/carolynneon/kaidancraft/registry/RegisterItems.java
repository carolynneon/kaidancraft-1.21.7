package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.item.AP1Item;
import carolynneon.kaidancraft.item.BoltItem;
import carolynneon.kaidancraft.list.ConsumableList;
import carolynneon.kaidancraft.list.FoodList;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class RegisterItems {
    public static final Item BOLT = register("bolt", BoltItem::new, new Item.Settings());
    public static final Item ONIGIRI = register("onigiri", Item::new, new Item.Settings().food(FoodList.ONIGIRI_COMPONENT, ConsumableList.ONIGIRI_COMPONENT));
    public static final Item AP1 = register("ap1", AP1Item::new, new Item.Settings().fireproof().rarity(Rarity.UNCOMMON).maxCount(1));

    public static <T extends Item> T register(String name, Function<Item.Settings, T> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, KaidanCraft.id(name));

        // Create the item instance.
        T item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void load() {
        CompostingChanceRegistry.INSTANCE.add(ONIGIRI, 0.85f);
    }
}
