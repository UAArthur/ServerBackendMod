package net.hauntedstudio.dcbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.hauntedstudio.dcbot.listener.ButtonInteractionListener;
import net.hauntedstudio.dcbot.listener.MessageListener;
import net.hauntedstudio.dcbot.slash.LinkCommand;
import net.hauntedstudio.manager.ConfigManager;
import org.jetbrains.annotations.NotNull;

public class Bot {

    private JDA jda;

    public Bot(String token) {
        if (token == null || token.isEmpty()) {
            System.out.println("Token cannot be null or empty");
        }
        if (token.equals("YOUR_DISCORD_BOT_TOKEN") || ConfigManager.isDefaultConfig) {
            System.out.println("Please provide a valid token");
            return;
        }

        JDABuilder builder = JDABuilder.createDefault(token);

        // Enable necessary intents
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

        // Enable other required intents
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.DIRECT_MESSAGES);


        // Set bot activity
        builder.setActivity(Activity.playing("Minecraft"));

        // Build JDA instance
        jda = builder.build();

        // Add event listeners
        jda.addEventListener(new MessageListener());
        jda.addEventListener(new LinkCommand());
        jda.addEventListener(new ButtonInteractionListener());
        // Add a listener for when all guild members are loaded
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildReady(@NotNull GuildReadyEvent event) {
                // Iterate over guilds and members here
                event.getGuild().loadMembers();
            }
        });

    }

    public JDA getJda() {
        return jda;
    }
}
