package co.zpdev.bots.jitters;

import co.zpdev.bots.jitters.lstnr.JoinLeaveLog;
import co.zpdev.bots.jitters.lstnr.ReadyListener;
import co.zpdev.bots.jitters.lstnr.RoleLog;
import co.zpdev.bots.jitters.lstnr.SpamLog;
import co.zpdev.core.discord.command.CommandHandler;
import co.zpdev.core.discord.exception.ExceptionHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ThreadLocalRandom;

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

        handler = new CommandHandler("!", "co.zpdev.bots.jitters.cmd");

        new JDABuilder(AccountType.BOT)
        .setToken(args[0])

        .setGame(Game.playing("v" + VERSION + " | !assign"))

        .setEventManager(new AnnotatedEventManager())

        .addEventListener(handler)

        .addEventListener(new ReadyListener())
        .addEventListener(new JoinLeaveLog())
        .addEventListener(new RoleLog())
        .addEventListener(new SpamLog())

        .build();
    }

}
