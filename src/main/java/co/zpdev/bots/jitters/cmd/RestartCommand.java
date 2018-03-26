package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.core.command.Command;
import co.zpdev.bots.core.exception.ExceptionHandler;
import co.zpdev.bots.jitters.util.MessageUtils;
import net.dv8tion.jda.core.entities.Message;

import java.io.InputStream;
import java.io.InputStreamReader;

public class RestartCommand {

    @Command(aliases = "restart")
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        try {
            MessageUtils.bypassLogs(message);

            InputStream is = Runtime.getRuntime().exec("/home/zp4rker/start-jitters.sh").getInputStream();
            InputStreamReader rd = new InputStreamReader(is);
            int c; StringBuilder sb = new StringBuilder();
            while ((c = rd.read()) != -1) {
                sb.append((char) c);
            }
            rd.close();

            String response = sb.toString().contains("Already up-to-date.") ? "" : "```Updated!```";
            message.getChannel().sendMessage("```" + sb.toString() + "```").complete();

            message.getJDA().shutdown();

            System.exit(0);
        } catch (Exception e) {
            ExceptionHandler.handleException("Restart command", e);
        }
    }

}