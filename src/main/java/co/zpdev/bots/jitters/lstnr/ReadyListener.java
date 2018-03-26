package co.zpdev.bots.jitters.lstnr;

import co.zpdev.bots.core.logger.ZLogger;
import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.jitters.util.UpcomingEpisode;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class ReadyListener {

    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        ZLogger.initialise();

        registerEventListeners(event.getJDA());

        Jitters.staff = Jitters.jda.getRoleById(312571560407990272L);

        UpcomingEpisode.start();

        clearExceptions(event.getJDA());

        event.getJDA().getPresence().setGame(Game.playing(Jitters.VERSION + " | !assign"));

        ZLogger.info("Jitters " + Jitters.VERSION + " started successfully!");
    }

    private void registerEventListeners(JDA jda) {
        jda.addEventListener(new JoinListener());
        jda.addEventListener(new LeaveListener());
        jda.addEventListener(new SpamListener());
        jda.addEventListener(new RoleListener());
    }

    private void clearExceptions(JDA jda) {
        jda.getUserById("145064570237485056").openPrivateChannel()
                .queue(pc -> pc.getHistory().retrievePast(50).queue(msgs -> msgs.forEach(m -> m.delete().queue())));
    }

}
