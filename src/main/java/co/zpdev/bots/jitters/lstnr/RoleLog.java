package co.zpdev.bots.jitters.lstnr;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;
import java.time.Instant;

public class RoleLog {

    @SubscribeEvent
    public void onRoleAdd(GuildMemberRoleAddEvent event) {
        if (event.getRoles().get(0).getName().equals("Member")) return;
        sendLog(event.getUser(), event.getRoles().get(0));
    }

    private void sendLog(User user, Role role) {
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setDescription("Role `" + role.getName() + "` added to " + user.getAsMention() + ".")
                .setColor(new Color(250, 166, 26))
                .setFooter("USERID: " + user.getId() + ", ROLEID: " + role.getId(), null)
                .setTimestamp(Instant.now()).build();

        user.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
