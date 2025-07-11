package carolynneon.kaidancraft.registry;

import carolynneon.kaidancraft.KaidanCraft;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.Optional;

public class RegisterItemGroups {
    public static final Text KAIDANCRAFT_ITEM_GROUP_TITLE = Text.translatable("itemgroup.kaidancraft");
    public static final RegistryKey<ItemGroup> KAIDANCRAFT_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), KaidanCraft.id("itemgroup"));
    public static final ItemGroup KAIDANCRAFT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(RegisterItems.BOLT::getDefaultStack)
            .displayName(KAIDANCRAFT_ITEM_GROUP_TITLE)
            .entries(((displayContext, entries) -> Registries.ITEM.getIds()
                    .stream().filter(key -> key.getNamespace().equals(KaidanCraft.MOD_ID))
                    .map(Registries.ITEM::getOptionalValue)
                    .map(Optional::orElseThrow)
                    .forEach(entries::add)))
            .build();

    public static void load() {
        // Register the group.
        Registry.register(Registries.ITEM_GROUP, KAIDANCRAFT_ITEM_GROUP_KEY, KAIDANCRAFT_ITEM_GROUP);

        // Register items to the custom item group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.END_CRYSTAL, RegisterItems.BOLT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addAfter(Items.BREAD, RegisterItems.ONIGIRI);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.TNT_MINECART, RegisterItems.AP1);
        });
    }
}
