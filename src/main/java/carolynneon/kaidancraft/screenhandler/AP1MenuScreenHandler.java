package carolynneon.kaidancraft.screenhandler;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.network.payload.EntityIDPayload;
import carolynneon.kaidancraft.registry.RegisterScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AP1MenuScreenHandler extends ScreenHandler {
    private final AP1Entity ap1Entity;
    private final ScreenHandlerContext context;
    private final PropertyDelegate propertyDelegate;
    protected final World world;

    //Client Constructor
    public AP1MenuScreenHandler(int syncId, PlayerInventory playerInventory, EntityIDPayload payload) {
        this(syncId, playerInventory, (AP1Entity) playerInventory.player.getWorld().getEntityById(payload.id()));
    }

    //Main Constructor (called from server)
    public AP1MenuScreenHandler(int syncId, PlayerInventory playerInventory, AP1Entity ap1Entity) {
        super(RegisterScreenHandlerTypes.AP1_MENU, syncId);

        this.ap1Entity = ap1Entity;
        this.context = ScreenHandlerContext.create(ap1Entity.getWorld(), ap1Entity.getBlockPos());
        this.propertyDelegate = ap1Entity.propertyDelegate;
        this.world = playerInventory.player.getWorld();

        checkSize(ap1Entity, 28);
        ap1Entity.onOpen(playerInventory.player);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(ap1Entity, j + (i * 9), 8 + j * 18, 75 + i * 18));
            }
        }
        this.addSlot(new AP1FuelSlot(this, ap1Entity, 27, 174, 35));
        this.addPlayerInventorySlots(playerInventory, 8, 142);
        this.addPlayerHotbarSlots(playerInventory, 8, 200);
        this.addProperties(propertyDelegate);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.ap1Entity.onClose(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack inSlot = slot.getStack();
            newStack = inSlot.copy();
            if (slotIndex == 27) {
                if (!insertItem(inSlot, ap1Entity.size(), this.slots.size(), true)) {
                    if (!insertItem(inSlot, 0, ap1Entity.size() - 1, true)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (this.isFuel(inSlot)) {
                if (!insertItem(inSlot, 27, 28, false)) {
                    if (slotIndex < ap1Entity.size()) {
                        if (!insertItem(inSlot, ap1Entity.size(), this.slots.size(), true)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!insertItem(inSlot, 0, ap1Entity.size() - 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (slotIndex < ap1Entity.size()) {
                    if (!insertItem(inSlot, ap1Entity.size(), this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!insertItem(inSlot, 0, ap1Entity.size() - 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (inSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.distanceTo(ap1Entity) < 4.0;
    }

    public AP1Entity getAp1Entity() {
        return this.ap1Entity;
    }

    public boolean isFuel(ItemStack stack) {
        return stack.isOf(Items.LAVA_BUCKET) || stack.isOf(Items.CHARCOAL) || stack.isOf(Items.BLAZE_ROD) ||
                stack.isOf(Items.COAL) || stack.isOf(Items.COAL_BLOCK);
    }

    public float getFuelProgress() {
        int i = this.propertyDelegate.get(1);
        if (i == 0) {
            i = 200;
        }

        return MathHelper.clamp((float)this.propertyDelegate.get(0) / i, 0.0F, 1.0F);
    }

    public boolean isBurning() {
        return this.propertyDelegate.get(0) > 0;
    }
}
