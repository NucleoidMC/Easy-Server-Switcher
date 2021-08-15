package fr.catcore.ess1_17_1;

import fr.catcore.ess_common.VersionHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ESS1_17 implements ModInitializer {
    @Override
    public void onInitialize() {
        if (VersionHandler.is1_17()) {
            VersionHandler.runCommand = (command, obj) -> {
                if (obj instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) obj;
                    playerEntity.getServer().getCommandManager().execute(playerEntity.getCommandSource(), command);
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
