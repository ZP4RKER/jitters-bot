package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.jitters.Jitters;
import co.zpdev.core.discord.command.Command;
import co.zpdev.core.discord.exception.ExceptionHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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
        pc.getHistory().retrievePast(100).complete().forEach(m -> m.delete().queue());

        String intro;
        InputStream in = Jitters.class.getResourceAsStream("/intros.txt");
        int c; StringBuilder sb = new StringBuilder();

        try {
            while ((c = in.read()) != -1) sb.append((char) c);

            int rand = ThreadLocalRandom.current().nextInt(0, sb.toString().split("\n").length);
            intro =  Arrays.stream(sb.toString().split("\n")).filter(s -> !s.startsWith("//")).collect(Collectors.toList()).get(rand).replace("%user%", message.getAuthor().getAsMention());
        } catch (IOException e) {
            ExceptionHandler.handleException("reading file (intros.txt)", e);
            intro =  null;
        }

        pc.sendMessage(intro).complete();
    }

}
