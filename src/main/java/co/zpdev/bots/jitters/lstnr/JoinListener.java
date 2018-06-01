package co.zpdev.bots.jitters.lstnr;

import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.jitters.util.MessageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
                        "\nAccount created " + MessageUtil.toTimeString(creation) + " ago.")
                .setColor(new Color(67, 181, 129))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
