package co.zpdev.bots.jitters.util;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Util class for ease of use for dealing with messages from commands.
 *
 * TODO: Move to core?
 *
 * @author zpdev
 * @version 0.9_BETA
 */
public class MessageUtils {

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

    /**
     * Sends a permission error message.
     *
     * @param message message which caused the error
     */
    public static void sendPermError(Message message) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("No Permission")
                .setDescription("You don't have the permissions required to perform this action!")
                .setColor(new Color(240, 71, 71)).build();
        selfDestuct(15000, message.getChannel().sendMessage(embed).complete(), message);
    }

}
