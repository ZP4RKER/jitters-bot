package me.zp4rker.discord.jitters.util;

import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.jitters.Jitters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author ZP4RKER
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ZLogger.err("Encountered an exception! Sending stacktrace in DM.");
        sendDM(PasteUtil.paste(getStackTrace(e)));
    }

    private String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    private void sendDM(String message) {
        Jitters.jda.getUserById(145064570237485056L).openPrivateChannel().queue(s -> s.sendMessage(message).queue());
    }

}
