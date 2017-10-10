package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.jitters.cmd.KickCommand;
import me.zp4rker.discord.jitters.util.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

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
        else sendLog(event.getMember());
    }

    private void sendLog(Member member) {
        User user = member.getUser();
        Instant joined = member.getJoinDate().toInstant();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription(user.getAsMention() + " left the server." +
                        "\nUser joined " + toTimeString(joined) + " ago.")
                .setColor(new Color(240, 71, 71))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

    private static String toTimeString(Instant instant) {
        String endString = "";

        Instant now = Instant.now();
        long timePast = now.getEpochSecond() - instant.getEpochSecond();

        long days = TimeUnit.SECONDS.toDays(timePast);
        timePast -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS.toHours(timePast);
        timePast -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = TimeUnit.SECONDS.toMinutes(timePast);

        if (days + hours < 1) {
            timePast -= TimeUnit.MINUTES.toSeconds(minutes);
            long seconds = timePast;
            if (minutes > 0) endString = minutes + (minutes == 1 ? " minute" : " minutes") + " and ";
            endString += seconds + (seconds == 1 ? "second" : "seconds");
        } else {
            endString = days + (days == 1 ? " day, " : " days, ");
            endString += hours + (hours == 1 ? " hour " : " hours ") + "and ";
            endString += minutes + (minutes == 1 ? " minute" : " minutes");
        }

        return endString;
    }

}
