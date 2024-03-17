package net.hauntedstudio.event;

import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hauntedstudio.AppBackend;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.awt.*;

public class ServerPlayConnectionJoinHandler implements ServerPlayConnectionEvents.Join {
    public final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=64";

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        if (!AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang").isEmpty()
                || !AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang").equals(" ")
                || AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang") != null) {
            AppBackend.playerSettingsManager.setPlayerSetting(handler.player.getUuid(), "lang", "de");
        }
        AppBackend.webhookSender.sendEmbedMessagetoWebhook(handler.player.getName().getString() + " joined the server", handler.player.getName().getString(), handler.player.getUuid().toString(), Color.GREEN.getRGB());
    }
}
