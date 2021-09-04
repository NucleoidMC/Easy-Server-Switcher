package fr.catcore.ess_common;

import net.fabricmc.loader.api.FabricLoader;

public class VersionHandler {

    public static PacketSender packetSender;

    public static boolean is1_17() {
        return FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString().startsWith("1.17");
    }

    public static boolean is1_16() {
        return !is1_17();
    }

    public static void switchServer(Object player) {
        String serverName = is1_17() ? "play" : "dev";
        packetSender.sendPacket(serverName, player);
    }

    public interface PacketSender {
        void sendPacket(String serverName, Object player);
    }
}
