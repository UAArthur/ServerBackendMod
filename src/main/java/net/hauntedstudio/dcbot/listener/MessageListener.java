package net.hauntedstudio.dcbot.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.hauntedstudio.AppBackend;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.EventListener;
import java.util.Objects;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("Message received from " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay() + " in channel " + event.getChannel().getId());
        if (event.getChannel().getId().equals(AppBackend.configManager.getString("discord-channel-id"))) {
            if (event.getAuthor().isBot()) return;
            String message = event.getMessage().getContentDisplay();
            MutableText text = Text.literal("§7[§aDiscord§7] " + event.getAuthor().getName() + "§f: " + message);
            //check if text contains some kind of link
            if (AppBackend.configManager.getString("discord-support-links").equals("true") && (message.contains("https://") || message.contains("http://") || message.contains("www.") || message.contains(".com") || message.contains(".de"))) {
                text.setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message)));
            }
            //Broadcast message
            AppBackend.server.getPlayerManager().broadcast(text, false);
        }

    }
}
