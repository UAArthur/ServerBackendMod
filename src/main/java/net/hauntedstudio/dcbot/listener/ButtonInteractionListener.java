package net.hauntedstudio.dcbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.hauntedstudio.AppBackend;
import net.hauntedstudio.commands.LinkCommand;
import net.hauntedstudio.filemanager.LinksManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class ButtonInteractionListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String userName = event.getUser().getName();
        UUID uuid = UUID.fromString(LinkCommand.linkRequest.remove(userName));

        if (uuid == null) {
            event.reply("There was an error processing your request. Please try again.").queue();
            return;
        }

        if (!(LinkCommand.linkRequest.containsValue(uuid.toString()))) {
            event.reply("There was an error processing your request. Please try again.").queue();
        }

        LinksManager linksManager = new LinksManager();
        if (event.getComponentId().equals("accept")) {
            linksManager.addOrUpdateLink(event.getUser().getId(), uuid.toString());
            sendSuccessMessage(uuid, event, "Your account has been successfully linked!");
        } else if (event.getComponentId().equals("decline")) {
            linksManager.deleteLink(event.getUser().getId());
            sendDeclineMessage(uuid, event, "The request to link your account has been declined.");
        }
    }

    private void sendSuccessMessage(UUID uuid, ButtonInteractionEvent event, String message) {
        ServerPlayerEntity player = AppBackend.server.getPlayerManager().getPlayer(uuid);
        if (player != null) {
            player.sendMessage(Text.of("§7[§aDiscord§7] §7" + message), false);
        }
        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(buildAcceptedEmbed(Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()))).queue();
        event.reply(message).queue();
    }

    private void sendDeclineMessage(UUID uuid, ButtonInteractionEvent event, String message) {
        ServerPlayerEntity player = AppBackend.server.getPlayerManager().getPlayer(uuid);
        if (player != null) {
            player.sendMessage(Text.of("§7[§aDiscord§7] §7" + message), false);
        }
        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(buildDeclinedEmbed(Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()))).queue();
        event.reply(message).queue();
    }



    public MessageEmbed buildAcceptedEmbed(MessageEmbed.Footer footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Link Request Accepted!");
        embedBuilder.setDescription("Dein Account wurde erfolgreich verknüpft!");
        embedBuilder.setFooter(footer.getText(), footer.getIconUrl());
        embedBuilder.setColor(0x00FF00);

        return embedBuilder.build();
    }

    public MessageEmbed buildDeclinedEmbed(MessageEmbed.Footer footer) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Link Request Declined!");
        embedBuilder.setDescription("Dein Account wurde nicht verknüpft!");
        embedBuilder.setFooter(footer.getText(), footer.getIconUrl());
        embedBuilder.setColor(0xFF0000);

        return embedBuilder.build();
    }
}
