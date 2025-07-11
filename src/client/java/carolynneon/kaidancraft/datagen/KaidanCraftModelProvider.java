package carolynneon.kaidancraft.datagen;

import carolynneon.kaidancraft.registry.RegisterItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class KaidanCraftModelProvider extends FabricModelProvider {
    public KaidanCraftModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(RegisterItems.BOLT, Models.GENERATED);
        itemModelGenerator.register(RegisterItems.ONIGIRI, Models.GENERATED);
        itemModelGenerator.register(RegisterItems.AP1, Models.GENERATED);
    }
}
