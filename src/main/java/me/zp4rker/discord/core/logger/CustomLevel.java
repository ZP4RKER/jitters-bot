package me.zp4rker.discord.core.logger;

import java.util.logging.Level;

/**
 * @author ZP4RKER
 */
class CustomLevel extends Level {

    CustomLevel(String name) {
        super(name, Level.SEVERE.intValue() + 1);
    }

}
