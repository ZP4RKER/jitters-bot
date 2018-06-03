package co.zpdev.bots.jitters.util;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Util class for ease of use for dealing with messages from commands.
 *
 * @author zpdev
 * @version 0.9_BETA
 */
public class MessageUtil {

    /**
     * Deletes a bunch of messages silently (no logs).
     *
     * @deprecated until logs reimplemented
     * @param messages the messages to delete
     */
    public static void bypassLogs(Message... messages) {
        if (messages.length > 1) {
            messages[0].getTextChannel().deleteMessages(Arrays.asList(messages)).queue();
        } else {
            messages[0].delete().queue();
        }
    }

    /**
     * Auto-deletes message(s) after a specified time.
     *
     * @deprecated until logs reimplemented
     * @param life time in milliseconds
     * @param messages the message(s) to delete
     */
    public static void selfDestuct(long life, Message... messages) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                bypassLogs(messages);
            }
        }, life);
    }

    /**
     * Sends an error message with specified details.
     *
     * @param err short version of error (title)
     * @param content full error message
     * @param message message which caused the error
     */
    public static void sendError(String err, String content, Message message) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(err)
                .setDescription(content)
                .setFooter("{} = Required | [] = Optional", null)
                .setColor(new Color(240, 71, 71)).build();
        selfDestuct(15000, message.getChannel().sendMessage(embed).complete(), message);
    }

    public static String toTimeString(Instant instant) {
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
