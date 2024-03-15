package net.hauntedstudio.dcbot.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.hauntedstudio.AppBackend;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.EventListener;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("Message received from " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay() + " in channel " + event.getChannel().getId());
        if (event.getChannel().getId().equals(AppBackend.configManager.getString("discord-channel-id"))) {
            if (event.getAuthor().isBot()) return;
            AppBackend.server.getPlayerManager().broadcast(Text.of("§7[§aDiscord§7] " + event.getAuthor().getName() + "§f: " + event.getMessage().getContentDisplay()), false );
        }

    }
}
