package me.zp4rker.jittersbot.lstnr;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * @author ZP4RKER
 */
public class JoinListener {

    @SubscribeEvent
    public void onMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().getPublicChannel()
                .sendMessage("Welcome to Jitters, " + event.getUser().getAsMention() + "!").complete();
    }

}
