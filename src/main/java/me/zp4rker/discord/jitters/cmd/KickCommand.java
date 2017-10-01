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
public class KickCommand implements ICommand {

    @RegisterCommand(aliases = "kick", showInHelp = false)
    public void onCommand(Message message, String[] args) {
        if (!message.getMember().getRoles().contains(Jitters.getStaffRole())) {
            MessageUtils.sendPermError(message);
            return;
        }
        if (args.length < 2) {
            MessageUtils.sendError("Invalid arguments!", "Usage: `!kick {@user} {reason}`", message);
            return;
        }
        if (message.getMentionedUsers().size() < 1) {
            MessageUtils.sendError("Invalid arguments!", "You didn't mention any one!" +
                    "\nUsage: `!kick {@user} {reason}`", message);
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Member member = message.getMember();

        member.getGuild().getController().kick(member, reason).queue();
        message.addReaction("\uD83D\uDC4C").queue();
    }

    private void sendLog(Member kicked, User issuer, String reason) {
        // Log channel = 314654582183821312L
        User user = kicked.getUser();

        MessageEmbed embed = new EmbedBuilder()
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("Kicked by " + user.getAsMention() + "\n" +
                        "**Reason:** " + reason)
                .setColor(Color.RED)
                .setTimestamp(Instant.now()).build();

        kicked.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).complete();
    }

}