package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.jitters.lstnr.JoinLeaveLog;
import co.zpdev.core.discord.command.Command;
import co.zpdev.core.discord.exception.ExceptionHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author ZP4RKER
 */
public class TestCommand {

    @Command(
            aliases = "test",
            autodelete = true
    )
    public void onCommand(Message message) {
        PrivateChannel pc = message.getAuthor().openPrivateChannel().complete();

        String intro;
        pc.sendMessage(Jitters.class.getResource("intros.txt").toExternalForm()).complete();
        InputStream in = Jitters.class.getResourceAsStream("intros.txt");
        int c; StringBuilder sb = new StringBuilder();

        pc.sendMessage("start?").complete();
        try {
            while ((c = in.read()) != -1) sb.append((char) c);

            int rand = ThreadLocalRandom.current().nextInt(0, sb.toString().split("\n").length);
            intro =  Arrays.stream(sb.toString().split("\n")).filter(s -> !s.startsWith("//")).collect(Collectors.toList()).get(rand).replace("%user%", message.getAuthor().getAsMention());
        } catch (IOException e) {
            ExceptionHandler.handleException("reading file (intros.txt)", e);
            intro =  null;
        }
        pc.sendMessage("done?").complete();

        pc.sendMessage(intro).complete();
    }

}
