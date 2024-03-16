package net.hauntedstudio.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.filemanager.PlayerSettingsManager;
import net.hauntedstudio.utils.DiscordWebhook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.awt.*;

public class ServerPlayConnectionJoinHandler implements ServerPlayConnectionEvents.Join {
    public final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=64";

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        // Create a new DiscordWebhook instance
        DiscordWebhook webhook = new DiscordWebhook(AppBackend.webhookURL);

        // Create an EmbedObject instance
        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject()
                .setAuthor(handler.player.getName().getString() + " joined the game", null, LABYURL.replace("%uuid%", handler.player.getUuid().toString()))
                .setColor(Color.GREEN); // Set color
        // Add the embed to the webhook
        webhook.addEmbed(embed);

        try {
            // Execute the webhook
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang").isEmpty()
                || !AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang").equals(" ")
                || AppBackend.playerSettingsManager.getPlayerSetting(handler.player.getUuid(), "lang") != null) {
            AppBackend.playerSettingsManager.setPlayerSetting(handler.player.getUuid(), "lang", "de");
        }
    }
}
