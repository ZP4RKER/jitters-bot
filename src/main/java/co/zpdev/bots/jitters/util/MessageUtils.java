package co.zpdev.bots.jitters.util;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MessageUtils {

    public static void bypassLogs(Message... messages) {
        if (messages.length > 1) {
            messages[0].getTextChannel().deleteMessages(Arrays.asList(messages)).queue();
        } else {
            messages[0].delete().queue();
        }
    }

    public static void selfDestuct(long life, Message... messages) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                bypassLogs(messages);
            }
        }, life);
    }

    public static void sendError(String err, String content, Message message) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(err)
                .setDescription(content)
                .setFooter("{} = Required | [] = Optional", null)
                .setColor(new Color(240, 71, 71)).build();
        selfDestuct(15000, message.getChannel().sendMessage(embed).complete(), message);
    }

    public static void sendPermError(Message message) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("No Permission")
                .setDescription("You don't have the permissions required to perform this action!")
                .setColor(new Color(240, 71, 71)).build();
        selfDestuct(15000, message.getChannel().sendMessage(embed).complete(), message);
    }

}
