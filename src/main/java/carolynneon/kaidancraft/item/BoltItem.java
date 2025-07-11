package carolynneon.kaidancraft.item;

import carolynneon.kaidancraft.entity.projectile.BoltEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class BoltItem extends Item implements ProjectileItem {
    public static final int MIN_DRAW_DURATION = 5;
    public static final float MIN_POWER = 0.2F;

    public BoltItem(Item.Settings settings) {
        super(settings);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack = user.getStackInHand(playerEntity.getActiveHand());
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (i < MIN_DRAW_DURATION) {
                return false;
            } else {
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                if (world instanceof ServerWorld serverWorld) {
                    stack.decrementUnlessCreative(1, user);
                    BoltEntity boltEntity = ProjectileEntity.spawnWithVelocity(BoltEntity::new, serverWorld, itemStack, playerEntity, 0.0F, MIN_POWER + (0.025F * Math.min(i, 40)), 10.0F);
//                    if (playerEntity.isInCreativeMode()) {
//                        boltEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
//                    }

                    world.playSoundFromEntity(null, boltEntity, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.1F + (0.01F * Math.min(i, 40)), 0.25F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new BoltEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
}
