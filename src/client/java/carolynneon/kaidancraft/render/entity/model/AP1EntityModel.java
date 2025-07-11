package carolynneon.kaidancraft.render.entity.model;
import carolynneon.kaidancraft.KaidanCraft;
import carolynneon.kaidancraft.render.entity.state.AP1EntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class AP1EntityModel extends EntityModel<AP1EntityRenderState> {
    public static final EntityModelLayer AP1 = new EntityModelLayer(KaidanCraft.id("ap1"), "main");

//    private final ModelPart chassis;
//    private final ModelPart frame_left;
    private final ModelPart mudflap_left;
//    private final ModelPart frame_right;
    private final ModelPart mudflap_right;
//    private final ModelPart overhead_rack_back;
//    private final ModelPart overhead_rack_front;
    private final ModelPart seat_left;
    private final ModelPart seat;
    private final ModelPart seat_right;
    private final ModelPart seat2;
//    private final ModelPart tread_left;
//    private final ModelPart tread_right;
    private final ModelPart lever_left;
    private final ModelPart lever_right;

    public AP1EntityModel(ModelPart root) {
        super(root);
        ModelPart chassis = root.getChild("chassis");
        ModelPart frame_left = chassis.getChild("frame_left");
        this.mudflap_left = frame_left.getChild("mudflap_left");
        ModelPart frame_right = chassis.getChild("frame_right");
        this.mudflap_right = frame_right.getChild("mudflap_right");
//        this.overhead_rack_back = this.chassis.getChild("overhead_rack_back");
//        this.overhead_rack_front = this.chassis.getChild("overhead_rack_front");
        this.seat_left = root.getChild("seat_left");
        this.seat = this.seat_left.getChild("seat");
        this.seat_right = root.getChild("seat_right");
        this.seat2 = this.seat_right.getChild("seat2");
//        this.tread_left = root.getChild("tread_left");
//        this.tread_right = root.getChild("tread_right");
        this.lever_left = root.getChild("lever_left");
        this.lever_right = root.getChild("lever_right");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData chassis = modelPartData.addChild("chassis", ModelPartBuilder.create().uv(0, 65).cuboid(-7.5F, -38.0F, -4.5F, 15.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));

        ModelPartData frame_left = chassis.addChild("frame_left", ModelPartBuilder.create().uv(20, 75).cuboid(6.5F, -37.0F, -4.5F, 1.0F, 24.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 34).cuboid(6.5F, -13.0F, -19.5F, 7.0F, 7.0F, 24.0F, new Dilation(0.0F))
                .uv(26, 117).cuboid(7.5F, -13.0F, -20.5F, 5.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(62, 46).cuboid(6.5F, -7.0F, 4.5F, 7.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(76, 42).cuboid(8.5F, -9.0F, 11.5F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(68, 71).cuboid(6.0F, -23.25F, -20.5F, 8.0F, 10.0F, 9.0F, new Dilation(0.0F))
                .uv(72, 90).cuboid(8.0F, -23.25F, -4.75F, 6.0F, 10.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 117).cuboid(9.5F, -4.5F, -18.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(8, 117).cuboid(9.0F, -6.5F, -17.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 0.0F, 0.0F));

        ModelPartData wall_angle2_r1 = frame_left.addChild("wall_angle2_r1", ModelPartBuilder.create().uv(104, 27).cuboid(1.5F, -2.0F, -8.0F, 1.0F, 4.0F, 8.0F, new Dilation(0.0F))
                .uv(86, 27).cuboid(0.5F, -3.0F, -8.0F, 1.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(4.25F, -34.5F, 4.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData chain_r1 = frame_left.addChild("chain_r1", ModelPartBuilder.create().uv(62, 27).cuboid(-0.5F, -2.5F, -10.0F, 1.0F, 4.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(14.0F, -10.0F, -3.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData mudflap_left = frame_left.addChild("mudflap_left", ModelPartBuilder.create(), ModelTransform.origin(10.0F, -7.0F, 13.5F));

        ModelPartData mudflap_r1 = mudflap_left.addChild("mudflap_r1", ModelPartBuilder.create().uv(62, 42).cuboid(-3.5F, 0.0F, 0.0F, 7.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData frame_right = chassis.addChild("frame_right", ModelPartBuilder.create().uv(0, 75).cuboid(-7.5F, -37.0F, -4.5F, 1.0F, 21.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-13.5F, -16.0F, -19.5F, 7.0F, 10.0F, 24.0F, new Dilation(0.0F))
                .uv(14, 117).cuboid(-12.5F, -16.0F, -20.5F, 5.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(94, 41).cuboid(-12.5F, -15.0F, 4.5F, 5.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(62, 46).cuboid(-13.5F, -7.0F, 4.5F, 7.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(76, 42).cuboid(-11.5F, -9.0F, 11.5F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(68, 71).cuboid(-14.0F, -26.25F, -20.5F, 8.0F, 10.0F, 9.0F, new Dilation(0.0F))
                .uv(72, 90).cuboid(-14.0F, -26.25F, -4.75F, 6.0F, 10.0F, 9.0F, new Dilation(0.0F))
                .uv(8, 117).cuboid(-11.0F, -6.5F, -17.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 117).cuboid(-10.5F, -4.5F, -18.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 0.0F, 0.0F));

        ModelPartData wall_angle2_r2 = frame_right.addChild("wall_angle2_r2", ModelPartBuilder.create().uv(104, 27).cuboid(-2.5F, -2.0F, -8.0F, 1.0F, 4.0F, 8.0F, new Dilation(0.0F))
                .uv(86, 27).cuboid(-1.5F, -3.0F, -8.0F, 1.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-4.25F, -34.5F, 4.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData chain_r2 = frame_right.addChild("chain_r2", ModelPartBuilder.create().uv(62, 27).cuboid(-0.5F, -2.5F, -10.0F, 1.0F, 4.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(-14.0F, -10.0F, -3.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData mudflap_right = frame_right.addChild("mudflap_right", ModelPartBuilder.create(), ModelTransform.origin(-10.0F, -7.0F, 13.5F));

        ModelPartData mudflap_r2 = mudflap_right.addChild("mudflap_r2", ModelPartBuilder.create().uv(62, 42).cuboid(-3.5F, 0.0F, 0.0F, 7.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData overhead_rack_back = chassis.addChild("overhead_rack_back", ModelPartBuilder.create().uv(62, 60).cuboid(6.5F, -0.5F, 4.5F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(62, 60).cuboid(-7.5F, -0.5F, 4.5F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(62, 58).cuboid(-6.5F, -0.5F, 8.5F, 13.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(62, 58).cuboid(-6.5F, -0.5F, 13.5F, 13.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -36.0F, 0.0F));

        ModelPartData back_rung3_r1 = overhead_rack_back.addChild("back_rung3_r1", ModelPartBuilder.create().uv(62, 58).cuboid(0.5F, -1.0F, 7.0F, 13.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(48, 72).cuboid(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(48, 72).cuboid(13.5F, -1.0F, 0.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-7.0F, 0.5F, 14.5F, 0.3927F, 0.0F, 0.0F));

        ModelPartData overhead_rack_front = chassis.addChild("overhead_rack_front", ModelPartBuilder.create().uv(48, 81).cuboid(6.5F, -0.5F, -8.5F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 65).cuboid(11.5F, -0.5F, -14.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(48, 81).cuboid(-7.5F, -0.5F, -8.5F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 65).cuboid(-12.5F, -0.5F, -14.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(62, 56).cuboid(-11.5F, -0.5F, -9.5F, 23.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(62, 56).cuboid(-11.5F, -0.5F, -14.5F, 23.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -36.0F, 0.0F));

        ModelPartData front_rung3_r1 = overhead_rack_front.addChild("front_rung3_r1", ModelPartBuilder.create().uv(62, 56).cuboid(0.5F, -1.0F, -8.0F, 23.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(48, 72).cuboid(-0.5F, -1.0F, -8.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(48, 72).cuboid(23.5F, -1.0F, -8.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, 0.5F, -14.5F, -0.3927F, 0.0F, 0.0F));

        ModelPartData seat_left = modelPartData.addChild("seat_left", ModelPartBuilder.create().uv(44, 100).cuboid(-0.5F, -10.0F, -0.5F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(10.0F, 16.5F, 12.5F));

        ModelPartData seat = seat_left.addChild("seat", ModelPartBuilder.create().uv(58, 82).cuboid(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(40, 92).cuboid(-4.5F, -1.5F, -3.5F, 9.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(40, 86).cuboid(-4.5F, -6.5F, 2.5F, 9.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -10.0F, 0.0F));

        ModelPartData seat_sides_r1 = seat.addChild("seat_sides_r1", ModelPartBuilder.create().uv(94, 48).cuboid(-3.5F, -3.0F, -0.5F, 10.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -2.5F, 1.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData seat_right = modelPartData.addChild("seat_right", ModelPartBuilder.create().uv(44, 100).cuboid(-0.5F, -10.0F, -0.5F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(-10.0F, 16.5F, 12.5F));

        ModelPartData seat2 = seat_right.addChild("seat2", ModelPartBuilder.create().uv(58, 82).cuboid(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(40, 92).cuboid(-4.5F, -1.5F, -3.5F, 9.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(40, 86).cuboid(-4.5F, -6.5F, 2.5F, 9.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, -10.0F, 0.0F));

        ModelPartData seat_sides_r2 = seat2.addChild("seat_sides_r2", ModelPartBuilder.create().uv(94, 48).cuboid(-3.5F, -3.0F, -0.5F, 10.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -2.5F, 1.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData tread_left = modelPartData.addChild("tread_left", ModelPartBuilder.create().uv(62, 0).cuboid(7.0F, -6.0F, -10.5F, 6.0F, 6.0F, 21.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));

        ModelPartData back_r1 = tread_left.addChild("back_r1", ModelPartBuilder.create().uv(0, 108).cuboid(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.0F, 0.0F, 10.5F, 0.8727F, 0.0F, 0.0F));

        ModelPartData front_r1 = tread_left.addChild("front_r1", ModelPartBuilder.create().uv(22, 108).cuboid(-3.0F, -4.0F, -5.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.0F, 0.0F, -10.5F, -0.8727F, 0.0F, 0.0F));

        ModelPartData tread_right = modelPartData.addChild("tread_right", ModelPartBuilder.create().uv(62, 0).cuboid(-13.0F, -6.0F, -10.5F, 6.0F, 6.0F, 21.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));

        ModelPartData front_r2 = tread_right.addChild("front_r2", ModelPartBuilder.create().uv(22, 108).cuboid(-3.0F, -4.0F, -5.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-10.0F, 0.0F, -10.5F, -0.8727F, 0.0F, 0.0F));

        ModelPartData back_r2 = tread_right.addChild("back_r2", ModelPartBuilder.create().uv(0, 108).cuboid(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-10.0F, 0.0F, 10.5F, 0.8727F, 0.0F, 0.0F));

        ModelPartData lever_left = modelPartData.addChild("lever_left", ModelPartBuilder.create(), ModelTransform.origin(-8.5F, 13.5F, 5.0F));

        ModelPartData lever_r1 = lever_left.addChild("lever_r1", ModelPartBuilder.create().uv(106, 39).cuboid(-0.5F, -7.0F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        ModelPartData lever_right = modelPartData.addChild("lever_right", ModelPartBuilder.create(), ModelTransform.origin(-11.5F, 13.5F, 5.0F));

        ModelPartData lever_r2 = lever_right.addChild("lever_r2", ModelPartBuilder.create().uv(106, 39).cuboid(-0.5F, -7.0F, -0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(AP1EntityRenderState ap1EntityRenderState) {
        super.setAngles(ap1EntityRenderState);
        setMudFlapsAngle(ap1EntityRenderState.mudFlapsAngle, this.mudflap_left, this.mudflap_right);
        setSeatAngle(ap1EntityRenderState.rightSeatAngle, this.seat_right, this.seat2);
        setSeatAngle(ap1EntityRenderState.leftSeatAngle, this.seat_left, this.seat);
        setLeverAngle(ap1EntityRenderState.rightLeverAngle, this.lever_right);
        setLeverAngle(ap1EntityRenderState.leftLeverAngle, this.lever_left);
    }

    private static void setSeatAngle(float angle, ModelPart seat_pole, ModelPart seat_chair) {
        seat_pole.pitch = MathHelper.clampedLerp(0.0F, -1.0471976F, angle);
        seat_chair.pitch = MathHelper.clampedLerp(0.0F, 1.0471976F, angle);
    }

    private static void setLeverAngle(float angle, ModelPart lever) {
        lever.pitch = MathHelper.clampedLerp(0.0F, -0.5235988F, angle);
    }

    private static void setMudFlapsAngle(float angle, ModelPart mudflap_left, ModelPart mudflap_right) {
        mudflap_left.pitch = MathHelper.clampedLerp(-0.2617994F, 0.2617994F, (MathHelper.sin(-angle) + 1.0F) / 2.0F);
        mudflap_right.pitch = MathHelper.clampedLerp(-0.2617994F, 0.2617994F, (MathHelper.sin(-angle) + 1.0F) / 2.0F);
    }

//    @Override
//    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
//        chassis.render(matrices, vertexConsumer, light, overlay, color);
//        seat_left.render(matrices, vertexConsumer, light, overlay, color);
//        seat_right.render(matrices, vertexConsumer, light, overlay, color);
//        tread_left.render(matrices, vertexConsumer, light, overlay, color);
//        tread_right.render(matrices, vertexConsumer, light, overlay, color);
//        lever_left.render(matrices, vertexConsumer, light, overlay, color);
//        lever_right.render(matrices, vertexConsumer, light, overlay, color);
//    }
}
