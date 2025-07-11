package carolynneon.kaidancraft.network.payload;

import carolynneon.kaidancraft.KaidanCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AP1UpdateFuelPayload(int ap1Id, int litTimeRemaining, int litTotalTime) implements CustomPayload {
    public static final Id<AP1UpdateFuelPayload> AP1_UPDATE_FUEL_ID = new Id<>(KaidanCraft.id("ap1_update_fuel"));
    public static final PacketCodec<RegistryByteBuf, AP1UpdateFuelPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            AP1UpdateFuelPayload::ap1Id,
            PacketCodecs.VAR_INT,
            AP1UpdateFuelPayload::litTimeRemaining,
            PacketCodecs.VAR_INT,
            AP1UpdateFuelPayload::litTotalTime,
            AP1UpdateFuelPayload::new).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return AP1_UPDATE_FUEL_ID;
    }
}
