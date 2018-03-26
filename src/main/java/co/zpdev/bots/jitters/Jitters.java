package co.zpdev.bots.jitters;

import co.zpdev.bots.core.command.handler.CommandHandler;
import co.zpdev.bots.core.exception.ExceptionHandler;
import co.zpdev.bots.core.logger.ZLogger;
import co.zpdev.bots.jitters.lstnr.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import java.util.concurrent.ThreadLocalRandom;

public class Jitters {

    public static JDA jda;
    public static CommandHandler handler;
    public static final String VERSION = "v1.1.1";

    // Staff role
    public static Role staff;

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        handler = new CommandHandler("!", "co.zpdev.bots.jitters.cmd");

        jda = new JDABuilder(AccountType.BOT).setToken(args[0])
                .setEventManager(new AnnotatedEventManager())
                .addEventListener(handler)
                .addEventListener(new ReadyListener())
                .setBulkDeleteSplittingEnabled(false)
                .buildAsync();

        ZLogger.initialise();
    }

    private static String[] messages = new String[]{
            // Flash
            "My name is %user%. And I am the fastest man alive. To the outside world, I'm an ordinary forensic scientist. " +
                    "But secretly, with the help of my friends at S.T.A.R. Labs, I fight crime and find other metahumans like me.",
            // Arrow
            "My name is %user%. After five years in hell, I came home with only one goal: To save my city. Today I " +
                    "fight that war on two fronts. By day, I lead Star City as its mayor. But by night, I am someone else. I " +
                    "am something else.",
            // Supergirl
            "When I was a child, my planet Krypton was dying. I was sent to Earth to protect my cousin. But my pod got " +
                    "knocked off-course and by the time I got here, my cousin had already grown up and become... Superman. I hid " +
                    "who I really was until one day when an accident forced me to reveal myself to the world. I am %user%.",
            // Legends
            "Seriously, you idiots haven't figured this out by now? It all started when we blew up the time pigs, the " +
                    "Time Masters. Now history's all screwed up, but it's up to us to un-screw it up. But half the time we screw " +
                    "things up even worse. So don't call us heroes, we're something else. We're %user%.",
            // Constantine
            "My name is %user%. I am the one who steps on the shadows, all trench coat and arrogance. I'll drive your demons " +
                    "away, kick 'em in the bullocks, and spit on them when they're down, leaving only a nod and a wink and a wisecrack. " +
                    "I walk my path alone because, let's be honest... who would be crazy enough to walk it with me?"
    };

    public static String getWelcomeMessage(User user) {
        int max = messages.length;
        int rand = ThreadLocalRandom.current().nextInt(0, max);

        return messages[rand].replace("%user%", user.getAsMention());
    }

}
