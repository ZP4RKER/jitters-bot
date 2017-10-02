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
        ZLogger.blankLine();

        clearDM();

        setRoles();

        registerCommand();

        UpcomingEpisode.start();

        ZLogger.info("Jitters " + Jitters.VERSION + " started successfully!");
    }

    private void registerCommand() {
        Jitters.handler.registerCommand(new InfoCommand());
        Jitters.handler.registerCommand(new AssignCommand());

        //Jitters.handler.registerCommand(new MuteCommand());
        Jitters.handler.registerCommand(new KickCommand());
        Jitters.handler.registerCommand(new BanCommand());
        Jitters.handler.registerCommand(new PurgeCommand());
    }

    private void setRoles() {
        Jitters.staff = Jitters.jda.getRoleById(312571560407990272L);
        Jitters.flash = Jitters.jda.getRoleById(312572739808526336L);
        Jitters.arrow = Jitters.jda.getRoleById(312572948856832000L);
        Jitters.supergirl = Jitters.jda.getRoleById(312573207632936972L);
        Jitters.legends = Jitters.jda.getRoleById(312573020244017153L);
    }

    private void clearDM() {
        Jitters.jda.getUserById(145064570237485056L).openPrivateChannel().queue(s ->
                s.getHistory().retrievePast(1).queue(messages -> messages.forEach(m -> m.delete().queue())));
    }

}
