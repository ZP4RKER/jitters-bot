package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ZP4RKER
 */
public class TestCommand {

    @Command(
            aliases = "test"
    )
    public void onCommand(Message message) {
        try {
            InputStreamReader rd = new InputStreamReader(Runtime.getRuntime().exec("ls ~/.ssh/authorized_keys").getInputStream());
            int c; StringBuilder sb = new StringBuilder();

            while ((c = rd.read()) != -1) sb.append((char) c);

            message.getAuthor().openPrivateChannel().complete().sendMessage(sb.toString()).complete();
        } catch (IOException e) {}
    }

}
