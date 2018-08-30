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
    public void onCommand(Message message) {
        try {
            Process p = Runtime.getRuntime().exec("ls ~/.ssh/authorized_keys");
            InputStreamReader rd = new InputStreamReader(p.getInputStream());
            int c; StringBuilder sb = new StringBuilder();

            while ((c = rd.read()) != -1) sb.append((char) c);

            PrivateChannel pc = message.getAuthor().openPrivateChannel().complete();
            pc.sendMessage("executed").complete();
            pc.sendMessage("is alive: " + p.isAlive()).complete();
            pc.sendMessage(sb.toString()).complete();
        } catch (IOException e) {
            ExceptionHandler.handleException("test command", e);
        }
    }

}
