package net.hauntedstudio.manager;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.hauntedstudio.AppBackend;

public class DiscordWebhookSender {
    private static final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=32";

    public void sendMessagetoWebhook(String message, String username, String uuid) {
        WebhookMessage bMessage = new WebhookMessageBuilder()
                .setUsername(username) // use this username
                .setAvatarUrl(LABYURL.replace("%uuid%", uuid)) // use this avatar
                .setContent(escapeSpecialCharacters(message))
                .build();
        AppBackend.client.send(bMessage);
    }

    public void sendEmbedMessagetoWebhook(String message, String username, String uuid, int color) {
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder();
        builder.setColor(color);
        builder.setAuthor(new WebhookEmbed.EmbedAuthor(username, LABYURL.replace("%uuid%", uuid), null));
        builder.setDescription(message);
        AppBackend.client.send(builder.build());
    }
    public String escapeSpecialCharacters(String input) {
        // Escape double quotes and backslashes
        String escaped = input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        // You may need to handle other special characters depending on your use case
        return escaped;
    }

}
