package carolynneon.kaidancraft.network.payload;

import carolynneon.kaidancraft.KaidanCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AP1RenderStatePayload(int entityId, boolean mudFlapsMoving, boolean sittingRight, boolean sittingLeft, int leverRight, int leverLeft) implements CustomPayload {
    public static final CustomPayload.Id<AP1RenderStatePayload> AP1_RENDER_STATE_ID = new CustomPayload.Id<>(KaidanCraft.id("ap1_render_state"));
    public static final PacketCodec<RegistryByteBuf, AP1RenderStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            AP1RenderStatePayload::entityId,
            PacketCodecs.BOOLEAN,
            AP1RenderStatePayload::mudFlapsMoving,
            PacketCodecs.BOOLEAN,
            AP1RenderStatePayload::sittingRight,
            PacketCodecs.BOOLEAN,
            AP1RenderStatePayload::sittingLeft,
            PacketCodecs.VAR_INT,
            AP1RenderStatePayload::leverRight,
            PacketCodecs.VAR_INT,
            AP1RenderStatePayload::leverLeft,
            AP1RenderStatePayload::new).cast();

    public AP1RenderStatePayload(RegistryByteBuf buf) {
        this(buf.readVarInt(),
             buf.readBoolean(),
             buf.readBoolean(),
             buf.readBoolean(),
             buf.readVarInt(),
             buf.readVarInt());
    }

    // Instance method
    public void write(RegistryByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeBoolean(mudFlapsMoving);
        buf.writeBoolean(sittingRight);
        buf.writeBoolean(sittingLeft);
        buf.writeVarInt(leverRight);
        buf.writeVarInt(leverLeft);
    }

    // Static method, buffer-first
    public static void write2(RegistryByteBuf buf, AP1RenderStatePayload payload) {
        buf.writeVarInt(payload.entityId());
        buf.writeBoolean(payload.mudFlapsMoving());
        buf.writeBoolean(payload.sittingRight());
        buf.writeBoolean(payload.sittingLeft());
        buf.writeVarInt(payload.leverLeft());
        buf.writeVarInt(payload.leverRight());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return AP1_RENDER_STATE_ID;
    }
}
