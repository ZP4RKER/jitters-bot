package co.zpdev.bots.jitters.lstnr;

import co.zpdev.bots.jitters.util.UpcomingEpisode;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

public class ReadyListener {

    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        UpcomingEpisode.start(event.getJDA());
    }

}
