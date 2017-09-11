package me.zp4rker.discord.jitters.lstnr;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * @author ZP4RKER
 */
public class JoinListener {

    @SubscribeEvent
    public void onMemberJoin(GuildMemberJoinEvent event) {
        event.getGuild().getTextChannelById(312571375598698507L)
                .sendMessage("Welcome to Jitters, " + event.getUser().getAsMention() + "!" +
                        " Head on over to <#312817696578469888> and run the command `!sassign` to start adding your roles.").complete();
    }

}
