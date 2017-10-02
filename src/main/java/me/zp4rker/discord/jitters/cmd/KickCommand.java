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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZP4RKER
 */
public class KickCommand implements ICommand {

    public static List<String> kicked = new ArrayList<>();

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
        Member member = message.getGuild().getMember(message.getMentionedUsers().get(0));

        member.getGuild().getController().kick(member, reason).queue(s -> message.delete().queue());
        sendLog(member, message.getAuthor(), reason);
    }

    private void sendLog(Member kicked, User issuer, String reason) {
        // Log channel = 314654582183821312L
        User user = kicked.getUser();

        MessageEmbed embed = new EmbedBuilder()
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("Kicked by " + issuer.getAsMention() + ".\n" +
                        "Reason: " + reason)
                .setColor(new Color(240, 71, 71))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();

        kicked.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).queue(s -> KickCommand.kicked.add(user.getId()));
    }

}
