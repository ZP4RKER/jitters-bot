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
        if (event.getAuthor().getId().equals("145064570237485056")) return;

        String content = event.getMessage().getRawContent();
        // Invite links
        if (content.contains("discord.gg")) removeInvite(event.getMessage());
        if (content.contains("discordapp.com/invite")) removeInvite(event.getMessage());
        if (content.contains("discord.io")) removeInvite(event.getMessage());
        if (content.contains("discord.me")) removeInvite(event.getMessage());
        // Other stuff
        if (content.contains("This is memedog")) removeSpam(event.getMessage());
    }

    private void removeInvite(Message message) {
        TextChannel logs = Jitters.jda.getTextChannelById(314654582183821312L);

        message.delete().queue();
        String username = message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(username, null, message.getAuthor().getEffectiveAvatarUrl())
                .setDescription("Tried sending an invite link.")
                .setColor(new Color(240, 71, 71))
                .setTimestamp(Instant.now()).build();

        logs.sendMessage(embed).queue();
        message.getTextChannel().sendMessage(message.getAuthor().getAsMention() + ", please do not send invite links.").queue();
    }

    private void removeSpam(Message message) {
        TextChannel logs = Jitters.jda.getTextChannelById(314654582183821312L);

        message.delete().queue();
        String username = message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(username, null, message.getAuthor().getEffectiveAvatarUrl())
                .setDescription("Tried spamming.")
                .setColor(new Color(240, 71, 71))
                .setTimestamp(Instant.now()).build();

        logs.sendMessage(embed).queue();
        message.getTextChannel().sendMessage(message.getAuthor().getAsMention() + ", please do not spam.").queue();
    }

}
