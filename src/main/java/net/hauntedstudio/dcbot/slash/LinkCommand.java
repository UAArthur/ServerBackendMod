package net.hauntedstudio.dcbot.slash;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class LinkCommand extends ListenerAdapter {

    //TODO: Link the Minecraft account with the Discord account
    //TODO: First check if User is already linked
    //TODO: Check if User is online
    //TODO: Send Message to User, if he wants to link his account or not
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(event.getName().equals("link")){

            event.reply("Linking your account").queue();
        }
    }
}
