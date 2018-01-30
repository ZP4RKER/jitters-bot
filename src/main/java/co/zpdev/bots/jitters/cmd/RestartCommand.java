package co.zpdev.bots.jitters.cmd;

import co.zpdev.bots.core.command.Command;
import co.zpdev.bots.core.exception.ExceptionHandler;
import co.zpdev.bots.jitters.util.MessageUtils;
import net.dv8tion.jda.core.entities.Message;

public class RestartCommand {

    @Command(aliases = "restart")
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        try {
            MessageUtils.bypassLogs(message);

            Thread.sleep(1500);

            message.getJDA().shutdown();

            Runtime.getRuntime().exec("/home/zp4rker/start-jitters.sh").waitFor();

            System.exit(0);
        } catch (Exception e) {
            ExceptionHandler.handleException("Restart command", e);
        }
    }

}