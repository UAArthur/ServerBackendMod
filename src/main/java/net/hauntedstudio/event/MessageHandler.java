package net.hauntedstudio.event;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.hauntedstudio.AppBackend;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler implements ServerMessageEvents.ChatMessage {
    public static final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=64";
    private final ExecutorService executor = Executors.newCachedThreadPool();


    @Override
    public void onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        if (message.getContent().getString().startsWith("/"))
            return;
        AppBackend.webhookSender.sendMessagetoWebhook(message.getContent().getString(), sender.getName().getString(), sender.getUuid().toString());
    }




    // Cleanup resources
    public void shutdown() {
        executor.shutdown();
    }
}
