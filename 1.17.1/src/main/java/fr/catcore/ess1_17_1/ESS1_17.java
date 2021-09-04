package fr.catcore.ess1_17_1;

import fr.catcore.ess_common.VersionHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.nio.charset.StandardCharsets;

public class ESS1_17 implements ModInitializer {
    @Override
    public void onInitialize() {
        if (VersionHandler.is1_17()) {
            VersionHandler.packetSender = (serverName, obj) -> {
                if (obj instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) obj;
                    playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(new Identifier("nucleoid:switch_server"),
                            (PacketByteBuf) new PacketByteBuf(Unpooled.buffer()).writeBytes(serverName.getBytes(StandardCharsets.UTF_8))));
                }
            };
            Registry.register(Registry.ITEM, new Identifier("ess", "switcher"), new SwitchItem());
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                if (!handler.getPlayer().getInventory().contains(new ItemStack(new SwitchItem()))) {
                    handler.getPlayer().getInventory().insertStack(new ItemStack(new SwitchItem()));
                }
            });
        }
    }
}
