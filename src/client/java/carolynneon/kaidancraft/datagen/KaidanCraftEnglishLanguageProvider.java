package carolynneon.kaidancraft.datagen;

import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.registry.RegisterItemGroups;
import carolynneon.kaidancraft.registry.RegisterItems;
import carolynneon.kaidancraft.registry.RegisterSounds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class KaidanCraftEnglishLanguageProvider extends FabricLanguageProvider {
    public KaidanCraftEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value) {
        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
            builder.add(translatableTextContent.getKey(), value);
        } else {
            KaidanCraft.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(RegisterItems.BOLT, "Bolt");
        translationBuilder.add(RegisterItems.ONIGIRI, "Onigiri");
        translationBuilder.add(RegisterItems.AP1, "Tobacco Control Work Vehicle AP-1");
        translationBuilder.add(RegisterSounds.ENTITY_AP1_ENGINE, "Tobacco Control Work Vehicle AP-1 engine running");
        addText(translationBuilder, RegisterItemGroups.KAIDANCRAFT_ITEM_GROUP_TITLE, "KaidanCraft");
        addText(translationBuilder, AP1Entity.MENU_TITLE, "Tobacco Control Work Vehicle AP-1");
    }
}
