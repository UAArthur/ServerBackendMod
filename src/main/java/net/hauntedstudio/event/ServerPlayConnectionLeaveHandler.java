package net.hauntedstudio.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hauntedstudio.AppBackend;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.awt.*;

public class ServerPlayConnectionLeaveHandler implements ServerPlayConnectionEvents.Disconnect {
    public final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=64";
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        if (!AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang").isEmpty()
                || !AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang").equals(" ")
                || AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang") != null) {
            AppBackend.playerSettingsManager.setPlayerSetting(handler.player.getUuid(), "lang", "de");
        }
        AppBackend.webhookSender.sendEmbedMessagetoWebhook(handler.player.getName().getString() + " left the server", handler.player.getName().getString(), handler.player.getUuid().toString(), Color.RED.getRGB());
    }
}
