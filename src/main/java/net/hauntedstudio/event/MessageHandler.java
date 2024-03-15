package net.hauntedstudio.event;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.utils.DiscordWebhook;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public class MessageHandler implements ServerMessageEvents.ChatMessage {
    public final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=64";
    @Override
    public void onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        try {
            this.sendMessageDiscord(message.getContent().getString(), sender.getName().getString(), sender.getUuid().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageDiscord(String messageContent, String username, String userUUID) throws IOException {
        // Escape special characters in the message content
        String escapedContent = escapeSpecialCharacters(messageContent);

        // Construct the Discord webhook object
        DiscordWebhook webhook = new DiscordWebhook(AppBackend.webhookURL);
        webhook.setContent(escapedContent);
        webhook.setUsername(username);
        webhook.setAvatarUrl(LABYURL.replace("%uuid%", userUUID));

        // Execute the webhook
        webhook.execute();
    }

    private String escapeSpecialCharacters(String input) {
        // Escape double quotes and backslashes
        String escaped = input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        // You may need to handle other special characters depending on your use case
        return escaped;
    }

}
