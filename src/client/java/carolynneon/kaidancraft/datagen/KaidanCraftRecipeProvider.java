package carolynneon.kaidancraft.datagen;

import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.registry.RegisterItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KaidanCraftRecipeProvider extends FabricRecipeProvider {
    public KaidanCraftRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                createShaped(RecipeCategory.TOOLS, RegisterItems.BOLT, 4)
                        .pattern(" # ")
                        .pattern(" ##")
                        .pattern("#  ")
                        .input('#', Items.IRON_NUGGET)
                        .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                        .offerTo(exporter);
                createShaped(RecipeCategory.FOOD, RegisterItems.ONIGIRI)
                        .pattern(" # ")
                        .pattern("#D#")
                        .input('#', Items.WHEAT)
                        .input('D', Items.DRIED_KELP)
                        .criterion(hasItem(Items.KELP), conditionsFromItem(Items.KELP))
                        .offerTo(exporter);
                createShaped(RecipeCategory.TRANSPORTATION, RegisterItems.AP1)
                        .pattern("I#I")
                        .pattern("S S")
                        .pattern("C F")
                        .input('#', Items.IRON_BARS)
                        .input('I', Items.IRON_BLOCK)
                        .input('S', Items.SADDLE)
                        .input('C', Items.CHEST_MINECART)
                        .input('F', Items.FURNACE_MINECART)
                        .criterion(hasItem(Items.MINECART), conditionsFromItem(Items.MINECART))
                        .offerTo(exporter);
                offerSmelting(
                        List.of(RegisterItems.BOLT),
                        RecipeCategory.MISC,
                        Items.IRON_NUGGET,
                        0.1f,
                        200,
                        "bolt_to_iron_nugget"
                );
                offerBlasting(
                        List.of(RegisterItems.BOLT),
                        RecipeCategory.MISC,
                        Items.IRON_NUGGET,
                        0.1f,
                        100,
                        "bolt_to_iron_nugget"
                );
            }
        };
    }

    @Override
    public String getName() {
        return "KaidanCraftRecipeProvider";
    }
}