package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

/**
 * @author ZP4RKER
 */
public class TestCommand {

    @Command(
            aliases = "test",
            autodelete = true
    )
    public void onCommand(Message message, String[] args) {
        PrivateChannel pc = message.getAuthor().openPrivateChannel().complete();

        pc.getHistory().retrievePast(100).complete().forEach(m -> m.delete().queue());
    }

}
