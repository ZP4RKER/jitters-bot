package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.jitters.Jitters;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;

/**
 * @author ZP4RKER
 */
public class SpamListener {

    @SubscribeEvent
    public void onMessage(GuildMessageReceivedEvent event) {
        String content = event.getMessage().getRawContent();
        if (content.contains("discord.gg")) remove(event.getMessage());
        if (content.contains("discordapp.com/invite")) remove(event.getMessage());
        if (content.contains("discord.io")) remove(event.getMessage());
        if (content.contains("discord.me")) remove(event.getMessage());
    }

    private void remove(Message message) {
        TextChannel logs = Jitters.jda.getTextChannelById(314654582183821312L);
        
        message.delete().complete();
        String username = message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(username, null, message.getAuthor().getEffectiveAvatarUrl())
                .setDescription("Tried sending invite link.")
                .setColor(Color.RED)
                .setTimestamp(Instant.now()).build();

        logs.sendMessage(embed).queue();
    }

}
