package net.hauntedstudio.dcbot.listener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hauntedstudio.AppBackend;
import net.minecraft.text.*;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(AppBackend.configManager.getString("discord-channel-id"))) {
            if (!event.getAuthor().getId().equals("1011410213246603284") && event.getAuthor().isBot())
                return;

            String message = event.getMessage().getContentDisplay();
            MutableText text = Text.literal("§7[§a" + (event.getAuthor().getId().equals("1011410213246603284") ? "§d§lAI" : "Discord") + "§7] " + event.getAuthor().getName() + "§f: " + message);
            // Check if text contains some kind of link
            if (AppBackend.configManager.getString("discord-support-links").equals("true") && (message.contains("https://") || message.contains("http://") || message.contains("www.") || message.contains(".com") || message.contains(".de"))) {
                text.setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message)));
            }
            // Broadcast message
            AppBackend.server.getPlayerManager().broadcast(text, false);
        }
    }
}

