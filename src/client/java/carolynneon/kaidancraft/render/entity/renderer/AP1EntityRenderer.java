package carolynneon.kaidancraft.render.entity.renderer;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.render.entity.model.AP1EntityModel;
import carolynneon.kaidancraft.render.entity.state.AP1EntityRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class AP1EntityRenderer extends EntityRenderer<AP1Entity, AP1EntityRenderState> {
    private final Identifier texture;
    private final AP1EntityModel model;

    public AP1EntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 1.1F;
        this.texture = AP1EntityModel.AP1.id().withPath((path) -> {
            return "textures/entity/" + path + ".png";
        });
        this.model = new AP1EntityModel(ctx.getPart(AP1EntityModel.AP1));
    }

    public AP1EntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer) {
        super(ctx);
        this.shadowRadius = 1.1F;
        this.texture = layer.id().withPath((path) -> {
            return "textures/entity/" + path + ".png";
        });
        this.model = new AP1EntityModel(ctx.getPart(layer));
    }

    public void render(AP1EntityRenderState ap1EntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0F, 1.5F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - ap1EntityRenderState.yaw));
        float f = ap1EntityRenderState.damageWobbleTicks;
        if (f > 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(f) * f * ap1EntityRenderState.damageWobbleStrength / 10.0F * (float)ap1EntityRenderState.damageWobbleSide));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        EntityModel<AP1EntityRenderState> entityModel = this.getModel();
        entityModel.setAngles(ap1EntityRenderState);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.getRenderLayer());
        entityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(ap1EntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    protected EntityModel<AP1EntityRenderState> getModel() {
        return this.model;
    }

    protected RenderLayer getRenderLayer() {
        return this.model.getLayer(this.texture);
    }

    @Override
    public AP1EntityRenderState createRenderState() {
        return new AP1EntityRenderState();
    }

    public void updateRenderState(AP1Entity ap1Entity, AP1EntityRenderState ap1EntityRenderState, float f) {
        super.updateRenderState(ap1Entity, ap1EntityRenderState, f);
        ap1EntityRenderState.yaw = ap1Entity.getLerpedYaw(f);
        ap1EntityRenderState.damageWobbleTicks = (float) ap1Entity.getDamageWobbleTicks();
        ap1EntityRenderState.damageWobbleSide = ap1Entity.getDamageWobbleSide();
        ap1EntityRenderState.damageWobbleStrength = Math.max(ap1Entity.getDamageWobbleStrength() - f, 0.0F);
        ap1EntityRenderState.rightSeatAngle = ap1Entity.lerpRightSeatAngle(f);
        ap1EntityRenderState.leftSeatAngle = ap1Entity.lerpLeftSeatAngle(f);
        ap1EntityRenderState.rightLeverAngle = ap1Entity.lerpRightLeverAngle(f);
        ap1EntityRenderState.leftLeverAngle = ap1Entity.lerpLeftLeverAngle(f);
        ap1EntityRenderState.mudFlapsAngle = ap1Entity.lerpMudFlapsPhase(f);
//        boatEntityRenderState.yaw = abstractBoatEntity.getLerpedYaw(f);
//        boatEntityRenderState.damageWobbleTicks = (float)abstractBoatEntity.getDamageWobbleTicks() - f;
//        boatEntityRenderState.damageWobbleSide = abstractBoatEntity.getDamageWobbleSide();
//        boatEntityRenderState.damageWobbleStrength = Math.max(abstractBoatEntity.getDamageWobbleStrength() - f, 0.0F);
//        boatEntityRenderState.bubbleWobble = abstractBoatEntity.lerpBubbleWobble(f);
//        boatEntityRenderState.submergedInWater = abstractBoatEntity.isSubmergedInWater();
//        boatEntityRenderState.leftPaddleAngle = abstractBoatEntity.lerpPaddlePhase(0, f);
//        boatEntityRenderState.rightPaddleAngle = abstractBoatEntity.lerpPaddlePhase(1, f);
    }
}
