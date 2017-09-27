package me.zp4rker.discord.jitters;

import me.zp4rker.discord.core.logger.ZLogger;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author ZP4RKER
 */
public class UpcomingEpisode {

    private TextChannel flash;
    private TextChannel arrow;
    private TextChannel supergirl;
    private TextChannel legends;

    public UpcomingEpisode() {
        flash = Jitters.jda.getTextChannelById(312574911199576064L);
        arrow = Jitters.jda.getTextChannelById(312574944137707530L);
        supergirl = Jitters.jda.getTextChannelById(312575189877653504L);
        legends = Jitters.jda.getTextChannelById(312574974005346304L);
    }

    public void start() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTopics();
            }
        }, 0, 60000);
    }

    private void updateTopics() {
        flash.getManager().setTopic(getTopic("the-flash")).queue();
        arrow.getManager().setTopic(getTopic("arrow")).queue();
        supergirl.getManager().setTopic(getTopic("supergirl")).queue();
        legends.getManager().setTopic(getTopic("legends-of-tomorrow")).queue();
    }

    private String getTopic(String show) {
        JSONObject showData = readJsonFromUrl("http://api.tvmaze.com/search/shows?q=" + show);
        if (showData == null) return null;
        showData = showData.getJSONObject("show");

        String episodeUrl = showData.getJSONObject("_links").getJSONObject("nextepisode").getString("href");
        JSONObject episodeData = readJsonFromUrl(episodeUrl);
        if (episodeData == null) return null;

        String title = episodeData.getString("name");
        String season = episodeData.getInt("season") + "";
        String episode = episodeData.getInt("number") + "";

        String[] date = episodeData.getString("airdate").split("-");
        String[] time = episodeData.getString("airtime").split(":");

        Instant instant;
        try {
            instant = toInstant(date, time);
        } catch (Exception e) {
            ZLogger.warn("Could not get episode airdate/time!");
            return null;
        }

        String timeLeft = timeRemaining(instant);
        String episodeString = "S" + season + "E" + (episode.length() < 2 ? "0" : "") + episode + " - " + title;

        return "Next episode: In " + timeLeft + " (" + episodeString + ")";
    }

    private Instant toInstant(String[] date, String[] time) throws Exception {
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        return ZonedDateTime.of(LocalDateTime.of(year, month, day, hour, minute), ZoneId.of("GMT")).toInstant();
    }

    private String timeRemaining(Instant instant) {
        Instant now = Instant.now();
        long remaining = instant.getEpochSecond() - now.getEpochSecond();

        long days = TimeUnit.SECONDS.toDays(remaining);
        remaining -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS.toHours(remaining);
        remaining -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = TimeUnit.SECONDS.toMinutes(remaining);

        return days + "d " + hours + "h " + minutes + "m";
    }

    private JSONObject readJsonFromUrl(String url) {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            if (jsonText.startsWith("[")) return new JSONArray(jsonText).getJSONObject(0);
            else return new JSONObject(jsonText);
        } catch (Exception e) {
            ZLogger.warn("Could not get JSON from URL!");
            e.printStackTrace();
            return null;
        } finally {
            closeInputstream(is);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private void closeInputstream(InputStream is) {
        try {
            is.close();
        } catch (Exception e) {
            ZLogger.warn("Could not close inputstream!");
        }
    }

}
