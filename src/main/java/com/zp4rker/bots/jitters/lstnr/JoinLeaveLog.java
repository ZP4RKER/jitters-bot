package com.zp4rker.bots.jitters.lstnr;

import com.zp4rker.bots.jitters.Jitters;
import com.zp4rker.bots.jitters.cmd.Kick;
import com.zp4rker.core.discord.exception.ExceptionHandler;
import com.zp4rker.core.discord.util.TimeUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author ZP4RKER
 */
public class JoinLeaveLog {

    private static HashMap<String, Message> joinMessages = new HashMap<>();

    // Join Event
    @SubscribeEvent
    public void onJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(312571375598698507L);
        User user = event.getUser();
        Role role = event.getGuild().getRolesByName("Member", false).get(0);

        String intro = getIntro(user);
        if (intro == null) return;

        channel.sendMessage(intro).queue(m -> {
            joinMessages.put(user.getId(), m);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    joinMessages.remove(user.getId());
                }
            }, TimeUnit.DAYS.toMillis(2));
        });
        event.getGuild().getController().addSingleRoleToMember(event.getMember(), role).queue();

        Instant creation = user.getCreationTime().toInstant();
        String username = user.getName() + "#" + user.getDiscriminator();
        String avatarUrl = user.getEffectiveAvatarUrl();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(username, null, user.getEffectiveAvatarUrl())
                .setThumbnail(avatarUrl)
                .setDescription(user.getAsMention() + " joined the server." +
                        "\nAccount created " + TimeUtil.toString(creation, true) + " ago.")
                .setColor(new Color(67, 181, 129))
                .setFooter("USERID: " + user.getId(), null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

    // Leave Event
    @SubscribeEvent
    public void onLeave(GuildMemberLeaveEvent event) {
        String id = event.getUser().getId();

        if (joinMessages.containsKey(id)) {
            joinMessages.get(id).delete().queue(m -> joinMessages.remove(id));
        }

        if (Kick.kicked.contains(id)) {
            Kick.kicked.remove(id);
            return;
        }

        User user = event.getUser();
        String username = user.getName() + "#" + user.getDiscriminator();
        String avatarUrl = user.getEffectiveAvatarUrl();
        Instant joined = event.getMember().getJoinDate().toInstant();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(username, null, user.getEffectiveAvatarUrl())
                .setThumbnail(avatarUrl)
                .setDescription(user.getAsMention() + " left the server." +
                        "\nUser joined " + TimeUtil.toString(joined, true) + " ago.")
                .setColor(new Color(240, 71, 71))
                .setFooter("USERID: " + id, null)
                .setTimestamp(Instant.now()).build();
        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

    private String getIntro(User user) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(Jitters.class.getResourceAsStream("/intros.txt")));
        String line; List<String> intros = new ArrayList<>();

        try {
            while ((line = rd.readLine()) != null) {
                if (line.startsWith("//")) continue;
                intros.add(line);
            }
        } catch (IOException e) {
            ExceptionHandler.handleException("reading file (intros.txt)", e);
            return null;
        }

        int rand = ThreadLocalRandom.current().nextInt(0, intros.size());
        return intros.get(rand).replace("%user%", user.getAsMention());
    }

}
