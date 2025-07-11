package carolynneon.kaidancraft.entity.projectile;

import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.registry.RegisterEntityTypes;
import carolynneon.kaidancraft.registry.RegisterItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class BoltEntity extends ThrownItemEntity implements FlyingItemEntity {

    public BoltEntity(EntityType<? extends BoltEntity> entityType, World world) {
        super(entityType, world);
    }

    public BoltEntity(World world, LivingEntity owner, ItemStack stack) {
        super(RegisterEntityTypes.BOLT, owner, world, stack);
    }

    public BoltEntity(World world, double x, double y, double z) {
        super(RegisterEntityTypes.BOLT, x, y, z, world, new ItemStack(RegisterItems.BOLT));
    }

    public BoltEntity(World world, double x, double y, double z, ItemStack stack) {

        super(RegisterEntityTypes.BOLT, x, y, z, world, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return RegisterItems.BOLT;
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = new ItemStack(getDefaultItem());
        return new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            double d = 0.08;

            for (int i = 0; i < 8; i++) {
                this.getWorld()
                        .addParticleClient(
                                getParticleParameters(),
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                (this.random.nextFloat() - 0.5) * 0.08,
                                (this.random.nextFloat() - 0.5) * 0.08,
                                (this.random.nextFloat() - 0.5) * 0.08
                        );
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            entityHitResult.getEntity().damage(serverWorld, this.getDamageSources().thrown(this, this.getOwner()), 0.0F);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            ItemEntity itemEntity = EntityType.ITEM.create(this.getWorld(), SpawnReason.TRIGGERED);
            if (itemEntity != null) {
                itemEntity.setStack(new ItemStack(getDefaultItem()));
                itemEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                if (getOwner() != null) {
                    if (getOwner().isLiving()) {
                        LivingEntity user = (LivingEntity) getOwner();
                        if (!((LivingEntity) getOwner()).isInCreativeMode()) {
                            this.getWorld().spawnEntity(itemEntity);
                        }
                    } else {
                        this.getWorld().spawnEntity(itemEntity);
                    }
                } else {
                    this.getWorld().spawnEntity(itemEntity);
                }
            }
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }

}
