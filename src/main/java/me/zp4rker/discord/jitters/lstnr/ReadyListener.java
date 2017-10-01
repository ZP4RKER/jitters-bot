package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.jitters.Jitters;
import me.zp4rker.discord.jitters.UpcomingEpisode;
import me.zp4rker.discord.jitters.cmd.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * @author ZP4RKER
 */
public class ReadyListener {

    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        registerCommand();

        new UpcomingEpisode().start();

        ZLogger.blankLine();
        ZLogger.info("Jitters " + Jitters.VERSION + " started successfully!");
    }

    private void registerCommand() {
        Jitters.handler.registerCommand(new InfoCommand());
        Jitters.handler.registerCommand(new AssignCommand());

        Jitters.handler.registerCommand(new MuteCommand());
        Jitters.handler.registerCommand(new KickCommand());
        Jitters.handler.registerCommand(new BanCommand());
        Jitters.handler.registerCommand(new PurgeCommand());
    }

}
