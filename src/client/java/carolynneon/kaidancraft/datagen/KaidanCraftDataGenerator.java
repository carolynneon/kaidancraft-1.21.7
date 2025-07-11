package carolynneon.kaidancraft.datagen;

import carolynneon.kaidancraft.data.generator.KaidanCraftWorldGenerator;
import carolynneon.kaidancraft.registry.worldgen.RegisterConfiguredFeatures;
import carolynneon.kaidancraft.registry.worldgen.RegisterPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class KaidanCraftDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(KaidanCraftEnglishLanguageProvider::new);
        pack.addProvider(KaidanCraftEntityLootTableProvider::new);
        pack.addProvider(KaidanCraftEntityTagProvider::new);
        pack.addProvider(KaidanCraftModelProvider::new);
        pack.addProvider(KaidanCraftRecipeProvider::new);
        pack.addProvider(KaidanCraftWorldGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, RegisterConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, RegisterPlacedFeatures::bootstrap);
    }
}
