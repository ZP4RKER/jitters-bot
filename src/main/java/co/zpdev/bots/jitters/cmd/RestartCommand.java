package co.zpdev.bots.jitters.cmd;

import co.zpdev.core.discord.command.Command;
import co.zpdev.core.discord.exception.ExceptionHandler;
import co.zpdev.bots.jitters.util.MessageUtil;
import net.dv8tion.jda.core.entities.Message;

import java.io.InputStream;
import java.io.InputStreamReader;

public class RestartCommand {

    @Command(aliases = "restart", autodelete = true)
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        try {
            InputStream is = Runtime.getRuntime().exec("/home/zp4rker/start-jitters.sh").getInputStream();
            InputStreamReader rd = new InputStreamReader(is);
            int c; StringBuilder sb = new StringBuilder();
            while ((c = rd.read()) != -1) {
                sb.append((char) c);
            }
            rd.close();

            String response = sb.toString().contains("Already up-to-date.") ? "" : "```Updated!```";
            if (!response.isEmpty()) message.getChannel().sendMessage(response).complete();

            message.getJDA().shutdown();

            System.exit(0);
        } catch (Exception e) {
            ExceptionHandler.handleException("Restart command", e);
        }
    }

}