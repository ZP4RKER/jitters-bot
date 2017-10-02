package me.zp4rker.discord.core.logger;


import me.zp4rker.discord.jitters.Jitters;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ZP4RKER
 */
public class ZLogger {

    private static Logger logger;

    public static void initialise() {
        try {
            logger = Logger.getLogger("GBVerify");
            logger.addHandler(new FileHandler(new File(Jitters.getDirectory(), "latest.log").getPath()));
            ConsoleHandler cHandler = new ConsoleHandler();
            cHandler.setFormatter(new ZFormatter());
            logger.addHandler(cHandler);
            logger.setUseParentHandlers(false);
            // Starter blank line
            blankLine();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    public static void info(String message) {
        logger.info(message + "\n\n");
    }

    public static void warn(String message) {
        logger.warning(message + "\n\n");
    }

    public static void debug(String message) {
        logger.log(new CustomLevel("DEBUG"), message + "\n\n");
    }

    public static void blankLine() {
        System.out.println();
    }

}
