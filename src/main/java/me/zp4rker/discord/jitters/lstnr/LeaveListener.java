package me.zp4rker.discord.jitters.lstnr;

import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * @author ZP4RKER
 */
public class LeaveListener {

    @SubscribeEvent
    public void onLeave(GuildMemberLeaveEvent event) {
        String id = event.getUser().getId();
        if (JoinListener.joinMessages.containsKey(id)) {
            JoinListener.joinMessages.get(id).delete().queue();
            JoinListener.joinMessages.remove(id);
        }
    }

}
