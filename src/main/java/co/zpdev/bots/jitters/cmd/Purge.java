package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Purge {

    @Command(
            aliases = "purge",
            usage = "!purge <amount>",
            permission = Permission.MESSAGE_MANAGE,
            args = 1
    )
    public void onCommand(Message message, String[] args) {
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            error(message);
            return;
        }

        int count = 0;
        MessageHistory history = message.getTextChannel().getHistory();

        if (amount > 100) {
            while (amount > 100) {
                List<Message> messages = history.retrievePast(100).complete();
                if (messages.size() < 1) return;
                delete(messages);
                amount -= messages.size();
                count += messages.size();
            }

            if (amount > 0) {
                List<Message> messages = history.retrievePast(amount).complete();
                if (messages.size() < 1) return;
                delete(messages);
                count += messages.size();
            }
        } else {
            List<Message> messages = history.retrievePast(amount).complete();
            if (messages.size() < 1) return;
            delete(messages);
            count += messages.size();
        }

        if (count > 0) success(message, count);
    }

    private void success(Message message, int count) {
        MessageEmbed embed = new EmbedBuilder()
        .setColor(Color.YELLOW)
        .setTitle("Chat purged")
        .setDescription(message.getAuthor().getAsMention() + " purged the chat (" + message.getTextChannel().getAsMention() + ")")
        .setFooter(count + " messages deleted", null).build();

        message.getTextChannel().sendMessage(embed).queue(m -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                m.delete().queue();
            }
        }, TimeUnit.SECONDS.toMillis(10)));

        message.getGuild().getTextChannelsByName("logs", false).get(0).sendMessage(embed).complete();
    }

    private void error(Message message) {
        MessageEmbed embed = new EmbedBuilder()
        .setColor(new Color(240, 71, 71))
        .setTitle("Invalid Arguments")
        .setDescription("Invalid arguments! Correct usage: `!purge <amount>`").build();

        message.getTextChannel().sendMessage(embed).queue(m -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                message.delete().queue();
                m.delete().queue();
            }
        }, TimeUnit.SECONDS.toMillis(10)));
    }

    private void delete(List<Message> messages) {
        if (messages.size() == 1) messages.get(0).delete().complete();
        else messages.get(0).getTextChannel().deleteMessages(messages).complete();
    }

}
