package fr.catcore.ess1_17_1;

import fr.catcore.ess_common.VersionHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
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

public class ESS1_17 implements ModInitializer {
    @Override
    public void onInitialize() {
        if (VersionHandler.is1_17()) {
            VersionHandler.packetSender = (serverName, obj) -> {
                if (obj instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) obj;
                    ServerPlayNetworking.send(playerEntity, new Identifier("nucleoid:switch_server"), writeString(new PacketByteBuf(Unpooled.buffer()), serverName));
//                    playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(new Identifier("nucleoid:switch_server"),
//                            ));
                }
            };
            Registry.register(Registry.ITEM, new Identifier("ess", "switcher"), new SwitchItem());
//            ServerSidePacketRegistryImpl.INSTANCE.register(new Identifier("nucleoid:switch_server"),);

            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                if (!handler.getPlayer().getInventory().contains(new ItemStack(new SwitchItem()))) {
                    handler.getPlayer().getInventory().insertStack(new ItemStack(new SwitchItem()));
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
        return (PacketByteBuf) buf.writeBytes(baos.toByteArray());
    }
}
