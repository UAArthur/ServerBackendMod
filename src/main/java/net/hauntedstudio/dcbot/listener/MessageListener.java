package net.hauntedstudio.dcbot.listener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.manager.AIManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import java.util.Objects;

public class MessageListener extends ListenerAdapter {

    private final AIManager aiManager = new AIManager(); // Instantiate AIManager

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(AppBackend.configManager.getString("discord-channel-id"))) {
            if (event.getAuthor().isBot()) return;
            String message = event.getMessage().getContentDisplay();
            MutableText text = Text.literal("§7[§aDiscord§7] " + event.getAuthor().getName() + "§f: " + message);
            // Checks if Message is for AI
            if (message.startsWith("@Shiroo")) {
                aiManager.sendMessageToAI(null, event.getAuthor().getName(), message.replace("@Shiroo", ""));
            }
            // Check if text contains some kind of link
            if (AppBackend.configManager.getString("discord-support-links").equals("true") && (message.contains("https://") || message.contains("http://") || message.contains("www.") || message.contains(".com") || message.contains(".de"))) {
                text.setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message)));
            }
            // Broadcast message
            AppBackend.server.getPlayerManager().broadcast(text, false);
        }
    }

    // Cleanup resources
    public void shutdown() {
        aiManager.shutdown(); // Shutdown AIManager
    }
}
