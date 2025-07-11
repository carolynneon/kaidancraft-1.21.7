package carolynneon.kaidancraft.mixin;

import carolynneon.kaidancraft.entity.vehicle.AP1Entity;
import carolynneon.kaidancraft.entity.vehicle.AP1Part;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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