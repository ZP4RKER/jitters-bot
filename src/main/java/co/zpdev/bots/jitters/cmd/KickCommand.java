package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.jitters.util.MessageUtil;
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

public class KickCommand {

    public static List<String> kicked = new ArrayList<>();

    @Command(aliases = "kick")
    public void onCommand(Message message, String[] args) {
        if (!message.getMember().getRoles().contains(Jitters.staff)) {
            MessageUtil.sendPermError(message);
            return;
        }
        if (args.length < 2) {
            MessageUtil.sendError("Invalid arguments!", "Usage: `!kick {@user} {reason}`", message);
            return;
        }
        if (message.getMentionedUsers().size() < 1) {
            MessageUtil.sendError("Invalid arguments!", "You didn't mention any one!" +
                    "\nUsage: `!kick {@user} {reason}`", message);
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Member member = message.getGuild().getMember(message.getMentionedUsers().get(0));

        member.getGuild().getController().kick(member, reason).queue(s -> MessageUtil.bypassLogs(message));
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

        KickCommand.kicked.add(user.getId());
        kicked.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}