package me.zp4rker.discord.jitters.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import me.zp4rker.discord.jitters.util.ExceptionHandler;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author ZP4RKER
 */
public class RestartCommand implements ICommand {

    @RegisterCommand(aliases = "restart")
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        message.delete().queue();

        try {
            Runtime.getRuntime().exec("/home/bots/start-jitters.sh").waitFor();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }

        message.getJDA().shutdown();
        System.exit(0);
    }

}