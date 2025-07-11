package carolynneon.kaidancraft.mixin;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.server.world.ServerWorld.class)
public class ServerWorldMixin {
//	@Unique
//	final Int2ObjectMap<AP1Part> ap1Parts = new Int2ObjectOpenHashMap<>();
//
//	@Inject(at = @At("TAIL"), method = "startTracking")
//	private void startTrackingAP1(Entity entity, CallbackInfo info) {
//		if (entity instanceof AP1Entity ap1Entity) {
//			for (AP1Part ap1Part : ap1Entity.getParts()) {
//				this.ap1Parts.put(ap1Part.getId(), ap1Part);
//			}
//		}
//	}
//
//	@Inject(at = @At("TAIL"), method = "stopTracking")
//	private void stopTrackingAP1(Entity entity, CallbackInfo info) {
//		if (entity instanceof AP1Entity ap1Entity) {
//			for (AP1Part ap1Part : ap1Entity.getParts()) {
//				this.ap1Parts.remove(ap1Part.getId());
//			}
//		}
//	}
}