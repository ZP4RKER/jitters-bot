package com.zp4rker.bots.jitters;

import com.zp4rker.bots.jitters.lstnr.JoinLeaveLog;
import com.zp4rker.bots.jitters.lstnr.RoleLog;
import com.zp4rker.bots.jitters.lstnr.SpamLog;
import com.zp4rker.core.discord.command.CommandHandler;
import com.zp4rker.core.discord.exception.ExceptionHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;

/**
 * The official Discord bot for Jitters.
 *
 * @author zpdev
 */
public class Jitters {

    public static CommandHandler handler;
    public static final String VERSION = Jitters.class.getPackage().getImplementationVersion();

    public static void main(String[] args) throws LoginException {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        ExceptionHandler.init(args[1], "Jitters");

        handler = new CommandHandler("!", "com.zp4rker.bots.jitters.cmd");

        JDA jda = new JDABuilder(AccountType.BOT)
        .setToken(args[0])

        .setGame(Game.playing("v" + VERSION + " | !assign"))

        .setEventManager(new AnnotatedEventManager())

        .addEventListener(handler)

        .addEventListener(new JoinLeaveLog())
        .addEventListener(new RoleLog())
        .addEventListener(new SpamLog())

        .build();

        ShowUpdater updater = new ShowUpdater(jda);
        updater.start();
    }

}
