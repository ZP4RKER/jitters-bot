package me.zp4rker.discord.jitters;

import me.zp4rker.discord.core.command.handler.CommandHandler;
import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.jitters.lstnr.JoinListener;
import me.zp4rker.discord.jitters.lstnr.LeaveListener;
import me.zp4rker.discord.jitters.lstnr.ReadyListener;
import me.zp4rker.discord.jitters.lstnr.SpamListener;
import me.zp4rker.discord.jitters.util.ExceptionHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZP4RKER
 */
public class Jitters {

    public static ExecutorService async = Executors.newCachedThreadPool();

    public static JDA jda;
    public static CommandHandler handler;
    public static final String VERSION = "v1.0";

    // Roles
    public static Role staff, flash, arrow, supergirl, legends;

    public static void main(String[] args) throws Exception {
        handler = new CommandHandler("!");

        jda = new JDABuilder(AccountType.BOT).setToken(args[0])
                .setEventManager(new AnnotatedEventManager())
                .addEventListener(handler)
                .addEventListener(new ReadyListener())
                .addEventListener(new JoinListener())
                .addEventListener(new LeaveListener())
                .addEventListener(new SpamListener())
                .buildAsync();

        ZLogger.initialise();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

    public static File getDirectory() throws Exception {
        return new File(Jitters.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }

}
