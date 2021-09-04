package fr.catcore.ess1_16_5;

import fr.catcore.ess_common.VersionHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ESS1_16 implements ModInitializer {

    private Item SWITCHER;

    @Override
    public void onInitialize() {
        if (VersionHandler.is1_16()) {
            VersionHandler.packetSender = (serverName, obj) -> {
                if (obj instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) obj;
                    ServerPlayNetworking.send(playerEntity, new Identifier("nucleoid:switch_server"), writeString(new PacketByteBuf(Unpooled.buffer()), serverName));
                }
            };
            SWITCHER = Registry.register(Registry.ITEM, new Identifier("ess", "switcher"), new SwitchItem());
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                if (!handler.player.inventory.contains(new ItemStack(SWITCHER))) {
                    handler.player.inventory.insertStack(new ItemStack(SWITCHER));
                }
            });
        }
    }

    public static PacketByteBuf writeString(PacketByteBuf buf, String s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new DataOutputStream(baos).writeUTF(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e); // this should never happen with a ByteArrayOutputStream
        }
        buf.writeBytes(baos.toByteArray());
        return buf;
    }
}
