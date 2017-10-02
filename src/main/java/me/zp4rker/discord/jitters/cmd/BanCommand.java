package me.zp4rker.discord.jitters.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import me.zp4rker.discord.jitters.Jitters;
import me.zp4rker.discord.jitters.util.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;

/**
 * @author ZP4RKER
 */
public class BanCommand implements ICommand {

    @RegisterCommand(aliases = "ban")
    public void onCommand(Message message, String[] args) {
        if (!message.getMember().getRoles().contains(Jitters.getStaffRole())) {
            MessageUtils.sendPermError(message);
            return;
        }
        if (args.length < 2) {
            MessageUtils.sendError("Invalid arguments!", "Usage: `!ban {@user} {reason}`", message);
            return;
        }
        if (message.getMentionedUsers().size() < 1) {
            MessageUtils.sendError("Invalid arguments!", "You didn't mention any one!" +
                    "\nUsage: `!ban {@user} {reason}`", message);
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Member member = message.getGuild().getMember(message.getMentionedUsers().get(0));

        message.getGuild().getController().ban(member, 7, reason).queue(s -> message.addReaction("\uD83D\uDC4C").queue());
        sendLog(member, message.getAuthor(), reason);
    }

    private void sendLog(Member baned, User issuer, String reason) {
        // Log channel = 314654582183821312L
        User user = baned.getUser();

        MessageEmbed embed = new EmbedBuilder()
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("Banned by " + issuer.getAsMention() + ".\n" +
                        "Reason: " + reason)
                .setColor(new Color(240, 71, 71))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();

        baned.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
