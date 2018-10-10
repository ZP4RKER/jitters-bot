package co.zpdev.bots.jitters;

import co.zpdev.core.discord.exception.ExceptionHandler;
import co.zpdev.core.discord.util.JSONUtil;
import co.zpdev.core.discord.util.PostUtil;
import co.zpdev.core.discord.util.TimeUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ZP4RKER
 */
class ShowUpdater {

    private JDA jda;
    private static JSONObject shows = null;

    ShowUpdater(JDA jda) {
        this.jda = jda;

        try {
            InputStreamReader rd = new InputStreamReader(Jitters.class.getResourceAsStream("/shows.json"));
            StringBuilder sb = new StringBuilder(); int c;

            while ((c = rd.read()) != -1) sb.append((char) c);

            shows = new JSONObject(sb.toString());
        } catch (IOException | JSONException e) {
            ExceptionHandler.handleException("reading json file (shows.json)", e);
        }
    }

    void start() {
        for (String show : shows.keySet()) {
            update(show);

            if (!shows.getJSONObject(show).has("nextepisode")) continue;

            long airTime = shows.getJSONObject(show).getJSONObject("nextepisode").getLong("airtime") - Instant.now().getEpochSecond();
            long fiveMin = airTime - TimeUnit.MINUTES.toSeconds(5);

            PostUtil.push("Time till airtime", "airtime = " + airTime + ", fiveMin = " + fiveMin);

            Timer timer = new Timer();
            if (fiveMin > 0) timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    announce(show, false);
                }
            }, fiveMin);
            if (airTime > 0) timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    announce(show, true);
                }
            }, airTime);
        }

        Timer timer = new Timer();

        shows.keySet().forEach(s -> timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update(s);
            }
        }, 0, TimeUnit.HOURS.toMillis(8)));

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTopics();
            }
        }, 0, TimeUnit.MINUTES.toMillis(1));
    }

    private void updateTopics() {
        for (String show : shows.keySet()) {
            if (!shows.getJSONObject(show).has("nextepisode")) continue;

            JSONObject data = shows.getJSONObject(show).getJSONObject("nextepisode");

            String topic = "Next episode in: " + TimeUtil.toString(Instant.ofEpochSecond(data.getLong("airtime")), true);
            topic += " (" + data.getString("number") + " \"" + data.getString("name") + "\")";

            jda.getTextChannelById(shows.getJSONObject(show).getLong("channel")).getManager().setTopic(topic).queue();
        }
    }

    /**
     * Announces the airing of a new episode and a 5 minute reminder beforehand
     *
     * @deprecated until fixed
     * @param show show to announce
     */
    private void announce(String show, boolean now) {
        if (!shows.getJSONObject(show).has("nextepisode")) return;
        if (!shows.getJSONObject(show).getJSONObject("nextepisode").has("airtime")) return;

        JSONObject data = shows.getJSONObject(show);
        JSONObject nextEp = data.getJSONObject("nextepisode");
        TextChannel c = jda.getTextChannelById(data.getLong("channel"));

        EmbedBuilder embed = new EmbedBuilder()
        .setColor(Color.decode(data.getString("colour")))
        .setTitle(data.getString("name"))
        .setFooter(data.getString("name") + " - " + nextEp.getString("number"), null)
        .setImage(data.getString("image"));

        if (!now) {
            embed.setDescription("\"" + nextEp.getString("name") + "\" starts in 5 minutes");
        } else {
            embed.setDescription("\"" + nextEp.getString("name") + "\" starts now");
        }

        PostUtil.push("Tried announcing for " + show, "now = " + now + ", airtime = " + nextEp.getLong("airtime") + ", Instant.now() = " + Instant.now().getEpochSecond());
        //c.sendMessage(embed.build()).queue();
    }

    private void update(String show) {
        JSONObject data = new JSONObject();

        data.put("id", shows.getJSONObject(show).getString("id"));
        data.put("channel", shows.getJSONObject(show).getLong("channel"));
        data.put("colour", shows.getJSONObject(show).getString("colour"));

        JSONObject sData = JSONUtil.fromUrl("http://api.tvmaze.com/shows/" + data.getString("id"));

        data.put("name", sData.getString("name"));
        data.put("image", sData.getJSONObject("image").getString("original"));
        data.put("summary", sData.getString("summary"));

        if (sData.getJSONObject("_links").has("nextepisode")) {
            JSONObject eData = JSONUtil.fromUrl(sData.getJSONObject("_links").getJSONObject("nextepisode").getString("href"));

            JSONObject nextEp = new JSONObject();
            nextEp.put("name", eData.getString("name"));
            nextEp.put("number", eData.getNumber("season") + "x" + eData.getNumber("number"));
            nextEp.put("airtime", getInstant(eData).getEpochSecond());
            if (!eData.isNull("summary")) nextEp.put("summary", eData.getString("summary"));
            data.put("nextepisode", nextEp);
        }

        shows.put(show, data);
    }

    private Instant getInstant(JSONObject eData) {
        int[] d = Arrays.stream(eData.getString("airdate").split("-")).mapToInt(Integer::parseInt).toArray();
        int[] t = eData.getString("airtime").isEmpty() || !eData.has("airtime") ? new int[]{0, 0} : Arrays.stream(eData.getString("airtime").split(":")).mapToInt(Integer::parseInt).toArray();

        return ZonedDateTime.of(LocalDateTime.of(d[0], d[1], d[2], t[0], t[1]), ZoneId.of("America/New_York")).toInstant();
    }

    private Date getDate(JSONObject eData) {
        int[] d = Arrays.stream(eData.getString("airdate").split("-")).mapToInt(Integer::parseInt).toArray();
        int[] t = eData.getString("airtime").isEmpty() || !eData.has("airtime") ? new int[]{0, 0} : Arrays.stream(eData.getString("airtime").split(":")).mapToInt(Integer::parseInt).toArray();

        Calendar c = Calendar.getInstance();
        c.set(d[0], d[1] - 1, d[2], t[0], t[1]);

        return c.getTime();
    }

}
