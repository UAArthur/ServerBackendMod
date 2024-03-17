package net.hauntedstudio.event;

import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.manager.AIManager;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler implements ServerMessageEvents.ChatMessage {
    public static final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=64";
    private final AIManager aiManager = new AIManager();
    private final ExecutorService executor = Executors.newCachedThreadPool();


    @Override
    public void onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        if (message.getContent().getString().startsWith("/"))
            return;
        if (message.getContent().getString().startsWith("@Shiroo") || message.getContent().getString().startsWith("@shiroo")) {
            aiManager.sendMessageToAI(sender.getUuid().toString(), sender.getName().getString(), message.getContent().getString().replace("@Shiroo", ""));
        }
        AppBackend.webhookSender.sendMessagetoWebhook(message.getContent().getString(), sender.getName().getString(), sender.getUuid().toString());
    }




    // Cleanup resources
    public void shutdown() {
        executor.shutdown();
    }
}
