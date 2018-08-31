package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.jitters.lstnr.JoinLeaveLog;
import co.zpdev.core.discord.command.Command;
import co.zpdev.core.discord.exception.ExceptionHandler;
import net.dv8tion.jda.core.entities.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ZP4RKER
 */
public class TestCommand {

    @Command(
            aliases = "test",
            autodelete = true
    )
    public void onCommand(Message message) {
        String intro;
        BufferedReader rd = new BufferedReader(new InputStreamReader(Jitters.class.getResourceAsStream("intros.txt")));
        String line; List<String> intros = new ArrayList<>();

        try {
            while ((line = rd.readLine()) != null) {
                if (line.startsWith("//")) continue;
                intros.add(line);
            }
            rd.close();

            int rand = ThreadLocalRandom.current().nextInt(0, intros.size());
            intro =  intros.get(rand).replace("%user%", message.getAuthor().getAsMention());
        } catch (IOException e) {
            ExceptionHandler.handleException("reading file (intros.txt)", e);
            intro =  null;
        }
        message.getAuthor().openPrivateChannel().complete().sendMessage(intro == null ? "null" : intro).complete();
    }

}
