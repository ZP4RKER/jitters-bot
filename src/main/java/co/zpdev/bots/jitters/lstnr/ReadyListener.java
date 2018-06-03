package co.zpdev.bots.jitters.lstnr;

import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.jitters.util.UpcomingEpisode;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class ReadyListener {

    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        registerEventListeners(event.getJDA());

        Jitters.staff = Jitters.jda.getRoleById(312571560407990272L);

        UpcomingEpisode.start();

        event.getJDA().getPresence().setGame(Game.playing("v" + Jitters.VERSION + " | !assign"));
    }

    private void registerEventListeners(JDA jda) {
        jda.addEventListener(new JoinListener());
        jda.addEventListener(new LeaveListener());
        jda.addEventListener(new SpamListener());
        jda.addEventListener(new RoleListener());
    }

}
