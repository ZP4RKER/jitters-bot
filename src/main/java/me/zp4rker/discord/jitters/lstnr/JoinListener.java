package me.zp4rker.discord.jitters.lstnr;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ZP4RKER
 */
public class JoinListener {

    static HashMap<String, Message> joinMessages = new HashMap<>();

    @SubscribeEvent
    public void onMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(312571375598698507L);
        String message = "Welcome to Jitters, " + event.getUser().getAsMention() + "!" + " Head on over to " +
                "<#312817696578469888> and run the command `!assign` to start adding your roles.";
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
    }

}
