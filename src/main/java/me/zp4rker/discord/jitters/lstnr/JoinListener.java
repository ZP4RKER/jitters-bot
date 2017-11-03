package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.jitters.Jitters;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author ZP4RKER
 */
public class JoinListener {

    static HashMap<String, Message> joinMessages = new HashMap<>();

    @SubscribeEvent
    public void onMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(312571375598698507L);
        String message = Jitters.getWelcomeMessage(event.getUser());
        channel.sendMessage(message).queue(m -> {
            joinMessages.put(event.getUser().getId(), m);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    joinMessages.remove(event.getUser().getId());
                }
            }, 172800000L);
        });
        Role role = event.getGuild().getRolesByName("Member", false).get(0);
        event.getGuild().getController().addSingleRoleToMember(event.getMember(), role).queue();

        sendLog(event.getUser());
    }

    private void sendLog(User user) {
        Instant creation = user.getCreationTime().toInstant();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription(user.getAsMention() + " joined the server." +
                        "\nAccount created " + toTimeString(creation) + " ago.")
                .setColor(new Color(67, 181, 129))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

    private void sendDM(User user) {
        try {
            String message = "Welcome to Jitters!";
            user.openPrivateChannel().queue(pc -> pc.sendMessage(message).queue());
        } catch (Exception e) {
            ZLogger.warn("Could not send DM to " + user.getId());
        }
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
            endString += seconds + (seconds == 1 ? " second" : " seconds");
        } else {
            endString = days + (days == 1 ? " day, " : " days, ");
            endString += hours + (hours == 1 ? " hour " : " hours ") + "and ";
            endString += minutes + (minutes == 1 ? " minute" : " minutes");
        }

        return endString;
    }

}
