package com.zp4rker.bots.jitters.cmd;

import com.zp4rker.core.discord.command.Command;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author ZP4RKER
 */
public class Kill {

    @Command(
            aliases = "kill",
            autodelete = true
    )
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        message.getChannel().sendMessage("Killing...").complete();
        message.getJDA().shutdown();

        System.exit(0);
    }

}
