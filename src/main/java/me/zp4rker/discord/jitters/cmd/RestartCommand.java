package me.zp4rker.discord.jitters.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import me.zp4rker.discord.jitters.util.ExceptionHandler;
import me.zp4rker.discord.jitters.util.MessageUtils;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author ZP4RKER
 */
public class RestartCommand implements ICommand {

    @RegisterCommand(aliases = "restart")
    public void onCommand(Message message) {
        if (!message.getAuthor().getId().equals("145064570237485056")) return;

        MessageUtils.selfDestuct(1, message);

        message.getJDA().shutdown();

        try {
            Runtime.getRuntime().exec("/home/bots/start-jitters.sh").waitFor();
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

}