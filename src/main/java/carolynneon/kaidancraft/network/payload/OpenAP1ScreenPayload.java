package carolynneon.kaidancraft.network.payload;

import carolynneon.kaidancraft.KaidanCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record OpenAP1ScreenPayload(int syncId, int ap1Id) implements CustomPayload {
    public static final CustomPayload.Id<OpenAP1ScreenPayload> OPEN_AP1_SCREEN_ID = new CustomPayload.Id<>(KaidanCraft.id("open_ap1_screen"));
    public static final PacketCodec<RegistryByteBuf, OpenAP1ScreenPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            OpenAP1ScreenPayload::syncId,
            PacketCodecs.VAR_INT,
            OpenAP1ScreenPayload::ap1Id,
            OpenAP1ScreenPayload::new).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }
}
