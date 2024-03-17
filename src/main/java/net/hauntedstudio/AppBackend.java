package net.hauntedstudio;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.hauntedstudio.commands.LinkCommand;
import net.hauntedstudio.commands.ServerBackendCommand;
import net.hauntedstudio.dcbot.Bot;
import net.hauntedstudio.event.MessageHandler;
import net.hauntedstudio.event.ServerPlayConnectionJoinHandler;
import net.hauntedstudio.event.ServerPlayConnectionLeaveHandler;
import net.hauntedstudio.manager.ConfigManager;
import net.hauntedstudio.manager.LanguageManager;
import net.hauntedstudio.manager.LinksManager;
import net.hauntedstudio.manager.PlayerSettingsManager;
import net.hauntedstudio.manager.DiscordWebhookSender;
import net.minecraft.server.MinecraftServer;

import java.io.File;

public class AppBackend implements ModInitializer {
    public static ConfigManager configManager;
    public static PlayerSettingsManager playerSettingsManager;
    public static LanguageManager languageManager;
    public static LinksManager linksManager;
    public static MinecraftServer server;

    //Var
    public static String webhookURL;
    public static String botToken;

    public static Bot bot; //Saves DiscordBot instance
    public static WebhookClient client; //Saves WebhookClient instance
    public static DiscordWebhookSender webhookSender = new DiscordWebhookSender();

    @Override
    public void onInitialize() {
            File linksFolder = new File("config/ServerBackendMod");
            if (!linksFolder.exists()) {
                linksFolder.mkdirs();
            }
            // Load Managers
            configManager = new ConfigManager();
            playerSettingsManager = new PlayerSettingsManager();
            languageManager = new LanguageManager();
            linksManager = new LinksManager();
            webhookURL = configManager.getString("discord-webhook-url");
            botToken = configManager.getString("discord-token");

            bot = new Bot(botToken);

            WebhookClientBuilder builder = new WebhookClientBuilder(webhookURL);
            builder.setThreadFactory((job) -> {
                Thread thread = new Thread(job);
                thread.setName("Webhook-Thread");
                thread.setDaemon(true);
                return thread;
            });
            builder.setWait(true);
            client = builder.build();

            System.out.println("Running on server!");
            ServerLifecycleEvents.SERVER_STARTING.register(server1 -> {
                server = server1;
            });

            // Register Commands
            ServerBackendCommand.registerCommand();
            LinkCommand.registerCommand();

            // Register Events
            ServerMessageEvents.CHAT_MESSAGE.register(new MessageHandler());
            ServerPlayConnectionEvents.JOIN.register(new ServerPlayConnectionJoinHandler());
            ServerPlayConnectionEvents.DISCONNECT.register(new ServerPlayConnectionLeaveHandler());
    }

}