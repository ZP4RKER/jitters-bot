package co.zpdev.bots.jitters.lstnr;

import co.zpdev.bots.jitters.cmd.KickCommand;
import co.zpdev.bots.jitters.util.MessageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class LeaveListener {

    @SubscribeEvent
    public void onLeave(GuildMemberLeaveEvent event) {
        String id = event.getUser().getId();

        // Join message delete handler
        if (JoinListener.joinMessages.containsKey(id)) {
            MessageUtil.bypassLogs(JoinListener.joinMessages.get(id));
            JoinListener.joinMessages.remove(id);
        }

        // Leave log handler
        if (KickCommand.kicked.contains(id)) KickCommand.kicked.remove(id);
        else sendLog(event.getMember());
    }

    private void sendLog(Member member) {
        User user = member.getUser();
        Instant joined = member.getJoinDate().toInstant();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription(user.getAsMention() + " left the server." +
                        "\nUser joined " + MessageUtil.toTimeString(joined) + " ago.")
                .setColor(new Color(240, 71, 71))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
