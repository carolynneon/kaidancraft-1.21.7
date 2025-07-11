package carolynneon.kaidancraft.mixin.client;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.network.payload.AP1RenderStatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Shadow
	public Input input;
	@Shadow
	private boolean riding;

	@Inject(method = "tickRiding", at = @At(value = "TAIL"))
	public void setAP1Inputs(CallbackInfo ci) {
		ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
		Entity var2 = self.getControllingVehicle();
		if (var2 instanceof AP1Entity ap1Entity) {
			ap1Entity.setInputs(this.input.playerInput.left(), this.input.playerInput.right(), this.input.playerInput.forward(), this.input.playerInput.backward());
			riding |= this.input.playerInput.left() || this.input.playerInput.right() || this.input.playerInput.forward() || this.input.playerInput.backward();
//			ClientPlayNetworking.send(new AP1RenderStatePayload(
//					ap1Entity.getId(),
//					ap1Entity.isMudFlapsMoving(),
//					ap1Entity.isSittingRight(),
//					ap1Entity.isSittingLeft(),
//					ap1Entity.getLeverRight(),
//					ap1Entity.getLeverLeft()
//			));
		}
	}
}