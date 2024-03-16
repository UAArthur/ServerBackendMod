package net.hauntedstudio.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.dcbot.Bot;
import net.hauntedstudio.filemanager.LanguageManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ServerBackendCommand {

    private final LanguageManager languageManager = AppBackend.languageManager;
    private static String playerLang;

    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            dispatcher.register(buildCommand());
        });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildCommand() {
        // Create a literal argument builder for the main command
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("ServerBackend").executes(context -> {
            // This is where you handle when no arguments are provided
            // You might want to display a usage message here
            return 0;
        });

        // /SB Setup Key Value
        LiteralArgumentBuilder<ServerCommandSource> setupArgument = literal("Setup");

        RequiredArgumentBuilder<ServerCommandSource, String> keyArgument = argument("key", StringArgumentType.string());
        RequiredArgumentBuilder<ServerCommandSource, String> valueArgument = argument("value", StringArgumentType.string())
                .executes(context -> sendSetupRequest(
                        context.getSource(),
                        StringArgumentType.getString(context, "key"),
                        StringArgumentType.getString(context, "value")));

        // Add subcommands to the main command
        setupArgument.then(keyArgument.then(valueArgument));
        literalArgumentBuilder.then(setupArgument);

        // /SB Info
        LiteralArgumentBuilder<ServerCommandSource> infoArgument = literal("Info")
                .executes(context -> sendInfoRequest(context.getSource()));

        // /SB QRCode
        LiteralArgumentBuilder<ServerCommandSource> qrcodeArgument = literal("QRCode")
                .executes(context -> sendQRCoderequest(context.getSource()));

        LiteralArgumentBuilder<ServerCommandSource> inviteArgument = literal("DCInvite")
                .executes(context -> sendDCInviteRequest(context.getSource()));


        // Add other subcommands to the main command
        literalArgumentBuilder.then(setupArgument);
        literalArgumentBuilder.then(infoArgument);
        literalArgumentBuilder.then(qrcodeArgument);
        literalArgumentBuilder.then(inviteArgument);

        return literalArgumentBuilder;
    }

    private static int sendDCInviteRequest(ServerCommandSource source) {
        playerLang = AppBackend.playerSettingsManager.getPlayerSetting(Objects.requireNonNull(source.getPlayer()).getUuid(), "lang");

        MutableText helloText = Text.literal("§7[§aDiscord§7] ").append(AppBackend.languageManager.getString(playerLang, "discord-invite-link"))
                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/oauth2/authorize?client_id=" + AppBackend.bot.getJda().getSelfUser().getId() + "&scope=bot&permissions=3072")));
        source.getPlayer().sendMessage(helloText, false);
        return 1;
    }

    // Methods for handling command executions
    private static int sendSetupRequest(ServerCommandSource source, String key, String value) {
        // Your logic for handling the /SB Setup command
        switch (key){
            case "discord-webhook-url": {
                if (!value.isEmpty()) {
                    AppBackend.configManager.addOrUpdateConfig("discord-webhook-url", value);
                }
                break;
            }
            case "discord-token": {
                if (!value.isEmpty()) {
                    AppBackend.configManager.addOrUpdateConfig("discord-token", value);
                }
                break;
            }
            case "discord-channel-id": {
                if (!value.isEmpty()) {
                    AppBackend.configManager.addOrUpdateConfig("discord-channel-id", value);
                }
                break;
            }
            case "discord-guild-id": {
                if (!value.isEmpty()) {
                    AppBackend.configManager.addOrUpdateConfig("discord-guild-id", value);
                }
                break;
            }
        }
        return 1; // Return 1 for success, adjust as needed
    }
    private static int sendInfoRequest(ServerCommandSource source) {
        // Your logic for handling the /SB Info command
        return 1; // Return 1 for success, adjust as needed
    }

    private static int sendQRCoderequest(ServerCommandSource source) {
        // Your logic for handling the /SB Login command
        return 1; // Return 1 for success, adjust as needed
    }
}
