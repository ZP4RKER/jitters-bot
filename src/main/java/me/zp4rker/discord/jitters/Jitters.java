package me.zp4rker.discord.jitters;

import me.zp4rker.discord.core.command.handler.CommandHandler;
import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.jitters.lstnr.*;
import me.zp4rker.discord.jitters.util.ExceptionHandler;
import me.zp4rker.discord.jitters.util.JSONUtil;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

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
                .addEventListener(new RoleListener())
                .buildAsync();

        ZLogger.initialise();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }



    public static String getWelcomeMessage(User user) {
        String message = "Welcome to Jitters, " + user.getAsMention() + "! Head on over to " +
                "<#312817696578469888> and run the command `!assign` to start adding your roles.";
        try {
            if (!configValid()) {
                generateConfig();
            }
            JSONObject data = JSONUtil.readFile(new File(getDirectory(), "conf.json"));
            String[] messages = toStringArray(data.getJSONArray("join-messages"));

            int max = messages.length;
            int rand = ThreadLocalRandom.current().nextInt(0, max);

            message = messages[rand].replace("%user%", user.getAsMention());
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return message;
    }

    private static String[] toStringArray(JSONArray array) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list.toArray(new String[0]);
    }

    private static void generateConfig() throws Exception {
        File configFile = new File(getDirectory(), "conf.json");
        if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdir();
        if (!configFile.exists()) configFile.createNewFile();

        // Generate default config
        JSONArray array = new JSONArray();
        array.put("My name is %user%. And I am the fastest man alive. To the outside world, I'm an ordinary forensic scientist. " +
                "But secretly, with the help of my friends at S.T.A.R. Labs, I fight crime and find other metahumans like me.");
        array.put("My name is %user%. After five years in hell, I came home with only one goal: To save my city. Today I " +
                "fight that war on two fronts. By day, I lead Star City as its mayor. But by night, I am someone else. I " +
                "am something else.");
        array.put("When I was a child, my planet Krypton was dying. I was sent to Earth to protect my cousin. But my pod got " +
                "knocked off-course and by the time I got here, my cousin had already grown up and become... Superman. I hid " +
                "who I really was until one day when an accident forced me to reveal myself to the world. I am %user%.");

        JSONObject root = new JSONObject();
        root.put("join-messages", array);

        JSONUtil.writeFile(root.toString(), configFile);
    }

    private static boolean configValid() {
        try {
            File configFile = new File(getDirectory(), "conf.json");
            if (!configFile.exists()) return false;

            JSONObject data = JSONUtil.readFile(configFile);
            return data.keySet().contains("join-messages") && data.get("join-messages") instanceof JSONArray;
        } catch (Exception e) {
            return false;
        }
    }

    public static File getDirectory() throws Exception {
        return new File(Jitters.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }

}
