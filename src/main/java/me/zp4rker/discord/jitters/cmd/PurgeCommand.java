package me.zp4rker.discord.jitters.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import me.zp4rker.discord.jitters.Jitters;
import me.zp4rker.discord.jitters.util.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZP4RKER
 */
public class PurgeCommand implements ICommand {

    private int count;

    @RegisterCommand(aliases = "purge")
    public void onCommand(Message message, String[] args) {
        if (!message.getMember().getRoles().contains(Jitters.getStaffRole())) {
            MessageUtils.sendPermError(message);
            return;
        }
        if (args.length < 1) {
            MessageUtils.sendError("Invalid arguments!", "Usage: `!purge [user] {#}`", message);
            return;
        }

        User user = null;
        if (message.getMentionedUsers().size() == 1) user = message.getMentionedUsers().get(0);

        int amount;
        try {
            if (user == null) amount = Integer.parseInt(args[0]);
            else amount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            MessageUtils.sendError("Invalid Arguments", "You provide valid arguments!" +
                    "\nCorrect usage: `sq!purge [user] {#}`", message);
            return;
        }

        MessageHistory history = message.getTextChannel().getHistory();

        List<Message> messages = new ArrayList<>();

        if (amount > 99) {
            messages.addAll(history.retrievePast(100).complete());
            while (amount > messages.size()) {
                if (((amount + 1) - messages.size()) > 100) messages.addAll(history.retrievePast(100).complete());
                else messages.addAll(history.retrievePast((amount + 1) - messages.size()).complete());
            }
        } else messages.addAll(history.retrievePast(amount + 1).complete());

        List<Message> toDelete = new ArrayList<>();

        if (user == null) toDelete.addAll(messages);
        else {
            messages.addAll(history.retrievePast(100).complete());
            messages.addAll(history.retrievePast(100).complete());
            messages.addAll(history.retrievePast(100).complete());
            final String id = user.getId();
            count = 0;
            messages.forEach(m -> {
                if (count < amount) {
                    if (m.getAuthor().getId().equals(id)) toDelete.add(m);
                    count++;
                }
            });
        }

        message.getTextChannel().deleteMessages(toDelete).complete();
        sendNotification(message, toDelete.size() - 1);
        sendLog(message.getMember(), message.getTextChannel(), toDelete.size() - 1);
    }

    private void sendLog(Member member, TextChannel channel, int amount) {
        // Log channel = 314654582183821312L
        User user = member.getUser();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("Channel purged", null, member.getGuild().getIconUrl())
                .setDescription(channel.getAsMention() + " purged by " + user.getAsMention() + ".\n" +
                        "**Messages:** " + amount + "\n")
                .setColor(Color.RED)
                .setTimestamp(Instant.now()).build();

        member.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).complete();
    }

    private void sendNotification(Message message, int amount) {
        MessageEmbed embed = new EmbedBuilder()
                .setDescription(message.getAuthor().getAsMention() + " just purged " + amount + " messages.")
                .setColor(Color.ORANGE).build();
        MessageUtils.selfDestuct(10000, message.getChannel().sendMessage(embed).complete());
    }

}
