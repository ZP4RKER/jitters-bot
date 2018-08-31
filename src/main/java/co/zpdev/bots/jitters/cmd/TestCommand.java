package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.jitters.lstnr.JoinLeaveLog;
import co.zpdev.core.discord.command.Command;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author ZP4RKER
 */
public class TestCommand {

    @Command(
            aliases = "test",
            autodelete = true
    )
    public void onCommand(Message message) {
        message.getAuthor().openPrivateChannel().complete().sendMessage(JoinLeaveLog.getIntro(message.getAuthor())).complete();
    }

}
