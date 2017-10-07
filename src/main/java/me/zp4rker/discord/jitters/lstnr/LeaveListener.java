package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.jitters.cmd.KickCommand;
import me.zp4rker.discord.jitters.util.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;

/**
 * @author ZP4RKER
 */
public class LeaveListener {

    @SubscribeEvent
    public void onLeave(GuildMemberLeaveEvent event) {
        String id = event.getUser().getId();

        // Join message delete handler
        if (JoinListener.joinMessages.containsKey(id)) {
            MessageUtils.selfDestuct(1, JoinListener.joinMessages.get(id));
            JoinListener.joinMessages.remove(id);
        }

        // Leave log handler
        if (KickCommand.kicked.contains(id)) KickCommand.kicked.remove(id);
        else sendLog(event.getUser());
    }

    private void sendLog(User user) {
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription(user.getAsMention() + " left the server.")
                .setColor(new Color(250, 166, 26))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
