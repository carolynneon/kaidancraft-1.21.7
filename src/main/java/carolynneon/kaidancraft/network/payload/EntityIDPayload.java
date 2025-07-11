package carolynneon.kaidancraft.network.payload;

import carolynneon.kaidancraft.KaidanCraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record EntityIDPayload(int id) implements CustomPayload {
    public static final CustomPayload.Id<EntityIDPayload> ENTITY_ID_PAYLOAD_ID = new CustomPayload.Id<>(KaidanCraft.id("entity_id"));
    public static final PacketCodec<ByteBuf, EntityIDPayload> CODEC = PacketCodecs.VAR_INT.xmap(EntityIDPayload::new, EntityIDPayload::id);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ENTITY_ID_PAYLOAD_ID;
    }
}
