package net.hauntedstudio.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.manager.LanguageManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class LinkCommand {

    /**
     * String = Discord username
     * String = Minecraft UUID of the player
     */

    public static final HashMap<String, String> linkRequest = new HashMap<>();
    private static final String LABYURL = "https://laby.net/texture/profile/head/%uuid%.png?size=32";

    public static void registerCommand(){
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            dispatcher.register(buildCommand());
        });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildCommand() {
        // Create a literal argument builder for the main command
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("link");

        // Define the username argument
        RequiredArgumentBuilder<ServerCommandSource, String> usernameArgument = RequiredArgumentBuilder
                .<ServerCommandSource, String>argument("username", StringArgumentType.string())
                .executes(context -> sendBindRequest(context.getSource(), StringArgumentType.getString(context, "username")));

        // Add the username argument to the main command
        literalArgumentBuilder.then(usernameArgument);

        return literalArgumentBuilder;
    }

    private static int sendBindRequest(ServerCommandSource serverCommandSourceCommandContext, String username) {
        LanguageManager languageManager = AppBackend.languageManager;
        String playerLang = AppBackend.playerSettingsManager.getPlayerSetting(Objects.requireNonNull(serverCommandSourceCommandContext.getPlayer()).getUuid(), "lang");
        if (linkRequest.containsKey(username)) {
            serverCommandSourceCommandContext.sendMessage(Text.of("§7[§aDiscord§7] " + languageManager.getString(playerLang, "discord-already_requested").replace("%username%", username)));
            return 1;
        }
        linkRequest.put(username, Objects.requireNonNull(serverCommandSourceCommandContext.getPlayer()).getUuidAsString());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(languageManager.getString(playerLang, "discord-link_request_title"));
        embedBuilder.setDescription(languageManager.getString(playerLang, "discord-link_request_description").replace("%username%", username).replace("%player_name%", serverCommandSourceCommandContext.getName()));
        embedBuilder.setFooter(languageManager.getString(playerLang, "discord-link_request_footer").replace("%player_name%", username), LABYURL.replace("%uuid%", serverCommandSourceCommandContext.getPlayer().getUuidAsString()));
        embedBuilder.setColor(Color.black);

        Button acceptButton = Button.success("accept", languageManager.getString(playerLang, "discord-accept_button"));
        Button declineButton = Button.danger("decline", languageManager.getString(playerLang, "discord-decline_button"));

        boolean userFound = false; // Flag to track if the user was found

        for (User user : AppBackend.bot.getJda().getUsers()) {
            if (user.getName().equals(username)) {
                userFound = true; // Set the flag to true since the user was found
                user.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessageEmbeds(embedBuilder.build())
                            .addActionRow(acceptButton, declineButton)
                            .queue();
                });
                break; // Exit the loop once the user is found
            }
        }

        if (!userFound) {
            serverCommandSourceCommandContext.sendMessage(Text.of("§7[§aDiscord§7] " + languageManager.getString(playerLang, "discord-user-not-found")));
            return 1; // Stop execution if the user was not found
        }

        serverCommandSourceCommandContext.sendMessage(Text.of("§7[§aDiscord§7] " + languageManager.getString(playerLang, "discord-link_request_sent").replace("%username%", username)));
        return 1;
    }

}
