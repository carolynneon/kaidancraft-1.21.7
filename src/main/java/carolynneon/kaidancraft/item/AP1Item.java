package carolynneon.kaidancraft.item;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.registry.RegisterEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AP1Item extends Item {

    public AP1Item(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return ActionResult.PASS;
        } else {
            Vec3d vec3d = user.getRotationVec(1.0F);
            double d = 5.0;
            List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0)).expand(1.0), EntityPredicates.CAN_HIT);
            if (!list.isEmpty()) {
                Vec3d vec3d2 = user.getEyePos();

                for (Entity entity : list) {
                    Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                    if (box.contains(vec3d2)) {
                        return ActionResult.PASS;
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                AP1Entity ap1Entity = this.createEntity(world, hitResult, itemStack, user);
                if (ap1Entity == null) {
                    return ActionResult.FAIL;
                } else {
                    ap1Entity.setYaw(user.getYaw());
                    if (!world.isSpaceEmpty(ap1Entity, ap1Entity.getBoundingBox())) {
                        return ActionResult.FAIL;
                    } else {
                        if (!world.isClient) {
                            world.spawnEntity(ap1Entity);
                            world.emitGameEvent(user, GameEvent.ENTITY_PLACE, hitResult.getPos());
                            itemStack.decrementUnlessCreative(1, user);
                        }

                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        return ActionResult.SUCCESS;
                    }
                }
            } else {
                return ActionResult.PASS;
            }
        }
    }

    @Nullable
    private AP1Entity createEntity(World world, HitResult hitResult, ItemStack stack, PlayerEntity player) {
        AP1Entity ap1Entity = RegisterEntityTypes.AP1.create(world, SpawnReason.SPAWN_ITEM_USE);
        if (ap1Entity != null) {
            Vec3d vec3d = hitResult.getPos();
            ap1Entity.initPosition(vec3d.x, vec3d.y, vec3d.z);
            if (world instanceof ServerWorld serverWorld) {
                EntityType.copier(serverWorld, stack, player).accept(ap1Entity);
            }
        }

        return ap1Entity;
    }
}
