package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.core.command.Command;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author ZP4RKER
 */
public class KillCommand {

    @Command(aliases = "kill")
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        message.delete().complete();
        message.getChannel().sendMessage("Killing...").complete();
        message.getJDA().shutdown();

        System.exit(0);
    }

}
