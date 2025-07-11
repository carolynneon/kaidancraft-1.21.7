package carolynneon.kaidancraft.entity.vehicle;

import carolynneon.kaidancraft.registry.RegisterEntityTypes;
import carolynneon.kaidancraft.screenhandler.AP1MenuScreenHandler;
import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.network.payload.EntityIDPayload;
import carolynneon.kaidancraft.registry.RegisterItems;
import carolynneon.kaidancraft.registry.RegisterSounds;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AP1Entity extends VehicleEntity implements ExtendedScreenHandlerFactory<EntityIDPayload>, VehicleInventory {
    public static final Text MENU_TITLE = Text.translatable("container." + KaidanCraft.MOD_ID + ".ap1_menu");
    private static final TrackedData<Boolean> MUD_FLAPS_MOVING = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SITTING_RIGHT = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SITTING_LEFT = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> LEVER_RIGHT = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> LEVER_LEFT = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> LIT_TIME_REMAINING = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> LIT_TOTAL_TIME = DataTracker.registerData(AP1Entity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final SoundEvent ENGINE_SOUND = RegisterSounds.ENTITY_AP1_ENGINE;

    public static final int BURN_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_TIME_PROPERTY_INDEX = 1;

    private float mudFlapsPhase;
    private float rightSeatAngle;
    private float leftSeatAngle;
    private float rightLeverAngle;
    private float leftLeverAngle;
    private float ticksUnderwater;

    private float yawVelocity;
    private final PositionInterpolator interpolator = new PositionInterpolator(this, 3);
    private boolean pressingLeft;
    private boolean pressingRight;
    private boolean pressingForward;
    private boolean pressingBack;
    private float nearbySlipperiness;
    private double waterLevel;
    private AP1Entity.Location location;
    private AP1Entity.Location lastLocation;

    @Nullable
    private RegistryKey<LootTable> lootTable;
    private long lootTableSeed;
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(28, ItemStack.EMPTY);

    int litTimeRemaining;
    int litTotalTime;
    public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return AP1Entity.this.litTimeRemaining;
                case 1:
                    return AP1Entity.this.litTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    AP1Entity.this.litTimeRemaining = value;
                    break;
                case 1:
                    AP1Entity.this.litTotalTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    private double fallVelocity;
    private boolean touchingWall;

    public AP1Entity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public AP1Entity(World world, double x, double y, double z) {
        this(RegisterEntityTypes.AP1, world);
        this.initPosition(x, y, z);
    }

    public void initPosition(double x, double y, double z) {
        this.setPosition(x, y, z);
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return MoveEffect.EVENTS;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MUD_FLAPS_MOVING, false);
        builder.add(SITTING_RIGHT, false);
        builder.add(SITTING_LEFT, false);
        builder.add(LEVER_RIGHT, 0);
        builder.add(LEVER_LEFT, 0);
        builder.add(LIT_TIME_REMAINING, 0);
        builder.add(LIT_TOTAL_TIME, 0);
    }

    @Override
    public boolean collidesWith(Entity other) {
        return canCollide(this, other);
    }

    public static boolean canCollide(Entity entity, Entity other) {
        return (other.isCollidable(entity) || other.isPushable()) && !entity.isConnectedThroughVehicle(other);
    }

    @Override
    public boolean isCollidable(@Nullable Entity entity) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.readInventoryFromData(view);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        this.writeInventoryToData(view);
    }

    @Override
    public Vec3d positionInPortal(Direction.Axis portalAxis, BlockLocating.Rectangle portalRect) {
        return LivingEntity.positionInPortal(super.positionInPortal(portalAxis, portalRect));
    }

    protected double getPassengerAttachmentY(EntityDimensions dimensions) {
        return dimensions.height() / 2.75F;
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        float x = -0.575F;
        float z = this.getPassengerHorizontalOffset();
        if (this.getPassengerList().size() > 1) {
            int i = this.getPassengerList().indexOf(passenger);
            if (i == 1) {
                x = 0.575F;
            }

            if (passenger instanceof AnimalEntity) {
                z -= 0.2F;
            }
        }

        return new Vec3d(x, this.getPassengerAttachmentY(dimensions), z).rotateY(-this.getYaw() * (float) (Math.PI / 180.0));
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (entity instanceof AP1Entity) {
            if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.pushAwayFrom(entity);
            }
        } else if (entity.getBoundingBox().minY <= this.getBoundingBox().maxY - 0.05) {
            if (entity.getBoundingBox().minY < this.getBoundingBox().minY + 0.5 && entity.getBoundingBox().maxY > this.getBoundingBox().maxY - 0.125F) {
                entity.setVelocity(entity.getVelocity().x, Math.min(entity.getVelocity().y, 0.0), entity.getVelocity().z);
            }
            double d = entity.getX() - this.getX();
            double e = entity.getZ() - this.getZ();
            Vec3d normalizedOffset = new Vec3d(d, 0.0, e).rotateY(this.getYaw() * (float) (Math.PI / 180.0));
            if (MathHelper.abs((float) normalizedOffset.z) > 1.2F) {
                if (MathHelper.abs((float) normalizedOffset.x) > 0.4F) {
                    pushAwayHelper(entity, 0.3, 1.5);
                }
            } else {
                if (MathHelper.abs((float) normalizedOffset.x) > 0.575F) {
                    pushAwayHelper(entity, 0.3, 1.5);
                } else if (MathHelper.abs((float) normalizedOffset.x) > 0.2F) {
                    double f = Math.sqrt(MathHelper.abs((float) normalizedOffset.x));
                    double g = 1.0 / f;
                    if (g > 1.0) {
                        g = 1.0;
                    }
                    double velX = normalizedOffset.x / f * g * 0.15F;
                    Vec3d pushVel = new Vec3d(-velX, 0.0, 0.0).rotateY(-this.getYaw() * (float) (Math.PI / 180.0));
                    if (!this.hasPassengers() && this.isPushable()) {
                        this.addVelocity(-pushVel.x * 0.3, 0.0, -pushVel.z * 0.3);
                    }

                    if (!entity.hasPassengers() && entity.isPushable()) {
                        entity.addVelocity(pushVel.x * 1.5, 0.0, pushVel.z * 1.5);
                    }
                }
            }
        } else {
            entity.setVelocity(entity.getVelocity().x, 0.0, entity.getVelocity().z);
            entity.onLanding();
        }
    }

    private void pushAwayHelper(Entity entity, double thisMultiplier, double entityMultiplier) {
        if (!this.isConnectedThroughVehicle(entity)) {
            if (!entity.noClip && !this.noClip) {
                double d = entity.getX() - this.getX();
                double e = entity.getZ() - this.getZ();
                double f = MathHelper.absMax(d, e);
                if (f >= 0.01F) {
                    f = Math.sqrt(f);
                    d /= f;
                    e /= f;
                    double g = 1.0 / f;
                    if (g > 1.0) {
                        g = 1.0;
                    }

                    d *= g;
                    e *= g;
                    d *= 0.05F;
                    e *= 0.05F;
                    if (!this.hasPassengers() && this.isPushable()) {
                        this.addVelocity(-d * thisMultiplier, 0.0, -e * thisMultiplier);
                    }

                    if (!entity.hasPassengers() && entity.isPushable()) {
                        entity.addVelocity(d * entityMultiplier, 0.0, e * entityMultiplier);
                    }
                }
            }
        }
    }

    @Override
    public void animateDamage(float yaw) {
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() * 11.0F);
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    public PositionInterpolator getInterpolator() {
        return this.interpolator;
    }

    @Override
    public Direction getMovementDirection() {
        return this.getHorizontalFacing().rotateYClockwise();
    }

    @Override
    public void tick() {
        this.lastLocation = this.location;
        this.location = this.checkLocation();
        if (this.location != AP1Entity.Location.UNDER_WATER && this.location != AP1Entity.Location.UNDER_FLOWING_WATER) {
            this.ticksUnderwater = 0.0F;
        } else {
            this.ticksUnderwater++;
        }

        if (!this.getWorld().isClient && this.ticksUnderwater >= 30.0F) {
            this.removeAllPassengers();
        }

        if (this.getDamageWobbleTicks() > 0) {
            this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
        }

        if (this.getDamageWobbleStrength() > 0.0F) {
            this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0F);
        }

        super.tick();
        this.interpolator.tick();
        if (this.isLogicalSideForUpdatingMovement()) {
            if (!(this.getFirstPassenger() instanceof PlayerEntity)) {
                this.setMudFlapsMoving(false);
            }

            this.updateVelocity();
            if (this.getWorld().isClient) {
                this.updateSeats();
                this.updateMudFlaps();
                this.updateLevers();
                //this.getWorld().sendPacket(new AP1RenderStateC2SPacket(this.getId(), this.isMudFlapsMoving(), this.isSittingRight(), this.isSittingLeft(), this.getLeverRight(), this.getLeverLeft()));
            }

            this.move(MovementType.SELF, this.getVelocity());
        } else {
            this.setVelocity(Vec3d.ZERO);
        }

        this.tickBlockCollision();
        this.tickBlockCollision();

        if (this.isMudFlapsMoving()) {
            this.mudFlapsPhase = this.mudFlapsPhase + (float) (Math.PI / 8);
        } else {
            this.mudFlapsPhase = 0.0F;
        }

        List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2F, -0.01F, 0.2F), EntityPredicates.canBePushedBy(this));
        if (!list.isEmpty()) {
            boolean bl = !this.getWorld().isClient && !(this.getControllingPassenger() instanceof PlayerEntity);

            for (Entity entity : list) {
                if (!entity.hasPassenger(this)) {
                    this.pushAwayFrom(entity);
                }
            }
        }

        if (this.getWorld().isClient) {
            if (!this.isSilent() && this.isBurning()) {
                this.getWorld().playSoundClient(this.getX(), this.getY(), this.getZ(), this.getEngineSound(), this.getSoundCategory(), 0.4F, 1.0F, false);
            }
        }
        this.updateFuel();
        if (this.getWorld().isClient && this.isBurning() && this.random.nextInt(8) == 0) {
            Vec3d offset = new Vec3d(-0.575, 0.8, 1.15).rotateY(-this.getYaw() * (float) (Math.PI / 180.0));
            Vec3d smokePos = new Vec3d(this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);
            this.getWorld().addParticleClient(ParticleTypes.LARGE_SMOKE, smokePos.x, smokePos.y, smokePos.z, 0.0, 0.0, 0.0);
        }
    }

    protected SoundEvent getEngineSound() {
        return ENGINE_SOUND;
    }

    public void setMudFlapsMoving(boolean moving) {
        this.dataTracker.set(MUD_FLAPS_MOVING, moving);
    }

    public float lerpMudFlapsPhase(float tickProgress) {
        return this.isMudFlapsMoving()
                ? MathHelper.clampedLerp(this.mudFlapsPhase - (float) (Math.PI / 8), this.mudFlapsPhase, tickProgress)
                : 0.0F;
    }

    public void setSittingRight(boolean sitting) {
        this.dataTracker.set(SITTING_RIGHT, sitting);
    }

    public float lerpRightSeatAngle(float tickProgress) {
        this.rightSeatAngle = this.isSittingRight()
                ? MathHelper.clampedLerp(this.rightSeatAngle, 1.0F, tickProgress)
                : MathHelper.clampedLerp(this.rightSeatAngle, 0.0F, tickProgress);
        return this.rightSeatAngle;
    }

    public void setSittingLeft(boolean sitting) {
        this.dataTracker.set(SITTING_LEFT, sitting);
    }

    public float lerpLeftSeatAngle(float tickProgress) {
        this.leftSeatAngle = this.isSittingLeft()
                ? MathHelper.clampedLerp(this.leftSeatAngle, 1.0F, tickProgress)
                : MathHelper.clampedLerp(this.leftSeatAngle, 0.0F, tickProgress);
        return this.leftSeatAngle;
    }

    public void setLeverRight(int value) {
        this.dataTracker.set(LEVER_RIGHT, value);
    }

    public float lerpRightLeverAngle(float tickProgress) {
        if (this.hasPassengers()) {
            switch (this.getLeverRight()) {
                case 1 -> this.rightLeverAngle = MathHelper.clampedLerp(this.rightLeverAngle, 0.0F, tickProgress);
                case 2 -> this.rightLeverAngle = MathHelper.clampedLerp(this.rightLeverAngle, 1.0F, tickProgress);
                default -> this.rightLeverAngle = MathHelper.clampedLerp(this.rightLeverAngle, 0.5F, tickProgress);
            }
            ;
        } else {
            this.rightLeverAngle = MathHelper.clampedLerp(this.rightLeverAngle, 0.0F, tickProgress);
        }
        return this.rightLeverAngle;
    }

    public float lerpLeftLeverAngle(float tickProgress) {
        if (this.hasPassengers()) {
            switch (this.getLeverLeft()) {
                case 1 -> this.leftLeverAngle = MathHelper.clampedLerp(this.leftLeverAngle, 0.0F, tickProgress);
                case 2 -> this.leftLeverAngle = MathHelper.clampedLerp(this.leftLeverAngle, 1.0F, tickProgress);
                default -> this.leftLeverAngle = MathHelper.clampedLerp(this.leftLeverAngle, 0.5F, tickProgress);
            };
        } else {
            this.leftLeverAngle = MathHelper.clampedLerp(this.leftLeverAngle, 0.0F, tickProgress);
        }
        return this.leftLeverAngle;
    }

    public void setLeverLeft(int value) {
        this.dataTracker.set(LEVER_LEFT, value);
    }

    private AP1Entity.Location checkLocation() {
        AP1Entity.Location location = this.getUnderWaterLocation();
        if (location != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return location;
        } else if (this.checkAP1InWater()) {
            return AP1Entity.Location.IN_WATER;
        } else {
            float f = this.getNearbySlipperiness();
            if (f > 0.0F) {
                this.nearbySlipperiness = f;
                return AP1Entity.Location.ON_LAND;
            } else {
                return AP1Entity.Location.IN_AIR;
            }
        }
    }

    public float getWaterHeightBelow() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(box.maxY - this.fallVelocity);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        label39:
        for (int o = k; o < l; o++) {
            float f = 0.0F;

            for (int p = i; p < j; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(p, o, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        f = Math.max(f, fluidState.getHeight(this.getWorld(), mutable));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }

            if (f < 1.0F) {
                return mutable.getY() + f;
            }
        }

        return l + 1;
    }

    public float getNearbySlipperiness() {
        Box box = this.getBoundingBox();
        Box box2 = new Box(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = MathHelper.floor(box2.minX) - 1;
        int j = MathHelper.ceil(box2.maxX) + 1;
        int k = MathHelper.floor(box2.minY) - 1;
        int l = MathHelper.ceil(box2.maxY) + 1;
        int m = MathHelper.floor(box2.minZ) - 1;
        int n = MathHelper.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = VoxelShapes.cuboid(box2);
        float f = 0.0F;
        int o = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int p = i; p < j; p++) {
            for (int q = m; q < n; q++) {
                int r = (p != i && p != j - 1 ? 0 : 1) + (q != m && q != n - 1 ? 0 : 1);
                if (r != 2) {
                    for (int s = k; s < l; s++) {
                        if (r <= 0 || s != k && s != l - 1) {
                            mutable.set(p, s, q);
                            BlockState blockState = this.getWorld().getBlockState(mutable);
                            if (!(blockState.getBlock() instanceof LilyPadBlock)
                                    && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(this.getWorld(), mutable).offset(mutable), voxelShape, BooleanBiFunction.AND)) {
                                f += blockState.getBlock().getSlipperiness();
                                o++;
                            }
                        }
                    }
                }
            }
        }

        return f / o;
    }

    private boolean checkAP1InWater() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.minY + 0.001);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        float f = p + fluidState.getHeight(this.getWorld(), mutable);
                        this.waterLevel = Math.max(f, this.waterLevel);
                        bl |= box.minY < f;
                    }
                }
            }
        }

        return bl;
    }

    @Nullable
    private AP1Entity.Location getUnderWaterLocation() {
        Box box = this.getBoundingBox();
        double d = box.maxY + 1.0;
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(d);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER) && d < mutable.getY() + fluidState.getHeight(this.getWorld(), mutable)) {
                        if (!fluidState.isStill()) {
                            return AP1Entity.Location.UNDER_FLOWING_WATER;
                        }

                        bl = true;
                    }
                }
            }
        }

        return bl ? AP1Entity.Location.UNDER_WATER : null;
    }

    @Override
    protected double getGravity() {
        return 0.04;
    }

    private void updateVelocity() {
        double d = -this.getFinalGravity();
        double e = 0.0;
        float f = 0.05F;
        if (this.lastLocation == AP1Entity.Location.IN_AIR
                && this.location != AP1Entity.Location.IN_AIR
                && this.location != AP1Entity.Location.ON_LAND) {
            this.waterLevel = this.getBodyY(1.0);
//            double g = this.getWaterHeightBelow() - this.getHeight() + 0.101;
//            if (this.getWorld().isSpaceEmpty(this, this.getBoundingBox().offset(0.0, g - this.getY(), 0.0))) {
//                this.setPosition(this.getX(), g, this.getZ());
//                this.setVelocity(this.getVelocity().multiply(1.0, 0.0, 1.0));
//                this.fallVelocity = 0.0;
//            }

            this.location = AP1Entity.Location.IN_WATER;
        } else {
            if (this.location == AP1Entity.Location.IN_WATER) {
                e = (this.waterLevel - this.getY()) / this.getHeight();
                f = 0.25F;
            } else if (this.location == AP1Entity.Location.UNDER_FLOWING_WATER) {
                d = -7.0E-4;
                f = 0.9F;
            } else if (this.location == AP1Entity.Location.UNDER_WATER) {
                e = 0.01F;
                f = 0.45F;
            } else if (this.location == AP1Entity.Location.IN_AIR) {
                f = 0.9F;
            } else if (this.location == AP1Entity.Location.ON_LAND) {
                f = this.nearbySlipperiness;
                if (this.getControllingPassenger() instanceof PlayerEntity) {
                    this.nearbySlipperiness /= 2.0F;
                }
            }

            Vec3d vec3d = new Vec3d(this.getVelocity().x * f,this.getVelocity().y + d, this.getVelocity().z * f);
            this.setVelocity(vec3d);
            this.yawVelocity *= f;
            if (e > 0.0) {
                Vec3d vec3d2 = this.getVelocity();
                this.setVelocity(vec3d2.x, (vec3d2.y + e * (this.getGravity() / 0.65)) * 0.75, vec3d2.z);
            }
        }
    }

    @Override
    public boolean isOnGround() {
        return nearbySlipperiness > 0.0F;
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    private void updateMudFlaps() {
        if (this.hasPassengers()) {
            if (this.getFirstPassenger() instanceof PlayerEntity && this.isBurning()) {
                float f = 0.0F;
                if (this.pressingLeft) {
                    this.yawVelocity--;
                }

                if (this.pressingRight) {
                    this.yawVelocity++;
                }

                this.setYaw(this.getYaw() + this.yawVelocity);
                if (this.pressingForward) {
                    f += 0.025F;
                }

                if (this.pressingBack) {
                    f -= 0.015F;
                }

                this.setVelocity(
                        this.getVelocity().add(MathHelper.sin(-this.getYaw() * (float) (Math.PI / 180.0)) * f, 0.0, MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0)) * f)
                );
                this.setMudFlapsMoving(this.pressingForward);
            }
        }
    }

    private void updateSeats() {
        this.setSittingRight(this.hasPassengers());
        this.setSittingLeft(this.getPassengerList().size() == this.getMaxPassengers());
    }

    private void updateLevers() {
        if (this.hasPassengers()) {
            int right = 0;
            int left = 0;
            if (this.pressingRight != this.pressingLeft && !this.pressingForward && !this.pressingBack) {
                right = this.pressingRight ? 1 : 2;
                left = this.pressingRight ? 2 : 1;
            } else if (this.pressingRight != this.pressingLeft && this.pressingForward && !this.pressingBack) {
                right = this.pressingRight ? 1 : 0;
                left = this.pressingRight ? 0 : 1;
            } else if (this.pressingRight != this.pressingLeft && !this.pressingForward && this.pressingBack) {
                right = this.pressingRight ? 0 : 2;
                left = this.pressingRight ? 2 : 0;
            } else if (this.pressingRight == this.pressingLeft && this.pressingForward != this.pressingBack) {
                right = this.pressingForward ? 1 : 2;
                left = this.pressingForward ? 1 : 2;
            }

            this.setLeverRight(right);
            this.setLeverLeft(left);
        } else {
            this.setLeverRight(0);
            this.setLeverLeft(0);

        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < this.getMaxPassengers() && !this.isSubmergedIn(FluidTags.WATER);
    }

    protected int getMaxPassengers() {
        return 2;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity livingEntity ? livingEntity : super.getControllingPassenger();
    }

    public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
    }

    @Override
    public boolean isSubmergedInWater() {
        return this.location == AP1Entity.Location.UNDER_WATER || this.location == AP1Entity.Location.UNDER_FLOWING_WATER;
    }

    @Override
    protected Item asItem() {
        return RegisterItems.AP1;
    }

    @Override
    public final ItemStack getPickBlockStack() {
        return new ItemStack(this.asItem());
    }

    protected float getPassengerHorizontalOffset() {
        return -1.25F;
    }

    public boolean isSmallerThanAP1(Entity entity) {
        return entity.getWidth() < this.getWidth();
    }
    @Override
    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (!passenger.getType().isIn(EntityTypeTags.CAN_TURN_IN_BOATS)) {
            passenger.setYaw(passenger.getYaw() + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.clampPassengerYaw(passenger);
            if (passenger instanceof AnimalEntity && this.getPassengerList().size() == this.getMaxPassengers()) {
                int i = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setBodyYaw(((AnimalEntity)passenger).bodyYaw + i);
                passenger.setHeadYaw(passenger.getHeadYaw() + i);
            }
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset(this.getWidth() * MathHelper.SQUARE_ROOT_OF_TWO, passenger.getWidth(), MathHelper.wrapDegrees(this.getYaw() + (passenger == this.getFirstPassenger() ? -150 : 150)));
        double d = this.getX() + vec3d.x;
        double e = this.getZ() + vec3d.z;
        BlockPos blockPos = BlockPos.ofFloored(passenger.getPos());
        BlockPos blockPos2 = blockPos.down();
        if (!this.getWorld().isWater(blockPos2)) {
            List<Vec3d> list = Lists.<Vec3d>newArrayList();
            double f = this.getWorld().getDismountHeight(blockPos);
            if (Dismounting.canDismountInBlock(f)) {
                list.add(new Vec3d(d, blockPos.getY() + f, e));
            }

            double g = this.getWorld().getDismountHeight(blockPos2);
            if (Dismounting.canDismountInBlock(g)) {
                list.add(new Vec3d(d, blockPos2.getY() + g, e));
            }

            for (EntityPose entityPose : passenger.getPoses()) {
                for (Vec3d vec3d2 : list) {
                    if (Dismounting.canPlaceEntityAt(this.getWorld(), vec3d2, passenger, entityPose)) {
                        passenger.setPose(entityPose);
                        return vec3d2;
                    }
                }
            }
        }

        return super.updatePassengerForDismount(passenger);
    }

    protected void clampPassengerYaw(Entity passenger) {
        passenger.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(passenger.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        passenger.lastYaw += g - f;
        passenger.setYaw(passenger.getYaw() + g - f);
        passenger.setHeadYaw(passenger.getYaw());
    }

    @Override
    public void onPassengerLookAround(Entity passenger) {
        this.clampPassengerYaw(passenger);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ActionResult actionResult = super.interact(player, hand);
        if (actionResult != ActionResult.PASS) {
            return actionResult;
        } else {
            if (getPassengerList().contains(player)) {
                return ActionResult.PASS;
            }
            if (player.isSneaking()) {
                if (this.getWorld().isClient()) {
                    return ActionResult.PASS;
                } else {
                    player.openHandledScreen(this);
                    return ActionResult.SUCCESS;
                }
            }
            return (ActionResult)(player.shouldCancelInteraction() || !(this.ticksUnderwater < 30.0F) || !this.getWorld().isClient && !player.startRiding(this)
                    ? ActionResult.PASS
                    : ActionResult.SUCCESS);
        }
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        super.move(type, movement);
        this.tickBlockCollision();
    }

    @Override
    public void tickBlockCollision() {
        super.tickBlockCollision();
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.minY + 0.001);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    BlockState block = this.getWorld().getBlockState(mutable);
                    if (block.getBlock() instanceof PlantBlock) {
                        getWorld().breakBlock(mutable, true, this);
                    }
                }
            }
        }
    }

    public boolean isMudFlapsMoving() {
        return this.dataTracker.get(MUD_FLAPS_MOVING) && this.getControllingPassenger() != null;
    }

    public boolean isSittingRight() {
        return this.dataTracker.get(SITTING_RIGHT) && this.getControllingPassenger() != null;
    }

    public boolean isSittingLeft() {
        return this.dataTracker.get(SITTING_LEFT) && this.getControllingPassenger() != null;
    }

    public int getLeverRight() {
        return this.getControllingPassenger() != null ? this.dataTracker.get(LEVER_RIGHT) : 0;
    }

    public int getLeverLeft() {
        return this.getControllingPassenger() != null ? this.dataTracker.get(LEVER_LEFT) : 0;
    }

    @Override
    public EntityIDPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new EntityIDPayload(this.getId());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        if (this.lootTable != null && player.isSpectator()) {
            return null;
        } else {
            this.generateInventoryLoot(playerInventory.player);
            return new AP1MenuScreenHandler(syncId, playerInventory, this);
        }
    }

    @Override
    public Text getDisplayName() {
        if (this.getCustomName() != null) {
            return this.getName();
        }
        return MENU_TITLE;
    }

    @Nullable
    @Override
    public RegistryKey<LootTable> getLootTable() {
        return this.lootTable;
    }

    @Override
    public void setLootTable(@Nullable RegistryKey<LootTable> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long lootTableSeed) {
        this.lootTableSeed = lootTableSeed;
    }

    @Override
    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void resetInventory() {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
    }

    @Override
    public int size() {
        return 28;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.getInventoryStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.removeInventoryStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.removeInventoryStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.setInventoryStack(slot, stack);
    }

    @Override
    public StackReference getStackReference(int mappedIndex) {
        return this.getInventoryStackReference(mappedIndex);
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.canPlayerAccess(player);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (!this.getWorld().isClient && reason.shouldDestroy()) {
            ItemScatterer.spawn(this.getWorld(), this, this);
        }

        super.remove(reason);
    }

    @Override
    public void killAndDropSelf(ServerWorld world, DamageSource damageSource) {
        super.killAndDropSelf(world, damageSource);
        this.onBroken(damageSource, world, this);
    }

    @Override
    public void clear() {
        this.clearInventory();
    }

    public void setLootTable(RegistryKey<LootTable> lootTable, long lootSeed) {
        this.lootTable = lootTable;
        this.lootTableSeed = lootSeed;
    }

    private void updateFuel() {
        boolean lit = this.isBurning();
        boolean allow_consuming_fuel = this.getFirstPassenger() instanceof PlayerEntity;
        if (lit) {
            this.litTimeRemaining--;
        }

        ItemStack fuelItemStack = this.getStack(27);
        boolean hasFuel = !fuelItemStack.isEmpty();
        if (lit || hasFuel) {
            if (!lit) {
                if (allow_consuming_fuel) {
                    this.litTimeRemaining = this.getFuelTime(this.getWorld().getFuelRegistry(), fuelItemStack);
                    this.litTotalTime = this.litTimeRemaining;
                    if (this.isBurning()) {
                        Item item = fuelItemStack.getItem();
                        fuelItemStack.decrement(1);
                        if (fuelItemStack.isEmpty()) {
                            this.setStack(27, item.getRecipeRemainder());
                        }
                    }
                }
            }
        }
        this.dataTracker.set(LIT_TIME_REMAINING, this.litTimeRemaining);
        this.dataTracker.set(LIT_TOTAL_TIME, this.litTotalTime);
    }

    private boolean isBurning() {
        return this.litTimeRemaining > 0;
    }

    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
        return fuelRegistry.getFuelTicks(stack);
    }

//    @Override
//    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
//        super.onSpawnPacket(packet);
//        AP1Part[] ap1Parts = this.getParts();
//
//        for (int i = 0; i < ap1Parts.length; i++) {
//            ap1Parts[i].setId(i + packet.getEntityId() + 1);
//        }
//    }

    public static enum Location {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
    }
}
