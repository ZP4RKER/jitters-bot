package co.zpdev.bots.jitters.util;

import co.zpdev.bots.core.logger.ZLogger;
import co.zpdev.bots.jitters.Jitters;
import co.zpdev.bots.core.exception.ExceptionHandler;
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

public class UpcomingEpisode {

    private static TextChannel flash, arrow, supergirl, legends;
    private static JSONObject flashData, arrowData, supergirlData, legendsData;

    public static void start() {
        // Initialisation
        flash = Jitters.jda.getTextChannelById(312574911199576064L);
        arrow = Jitters.jda.getTextChannelById(312574944137707530L);
        supergirl = Jitters.jda.getTextChannelById(312575189877653504L);
        legends = Jitters.jda.getTextChannelById(312574974005346304L);

        flashData = null;
        arrowData = null;
        supergirlData = null;
        legendsData = null;
        // Timer
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTopics();
            }
        }, 0, 60000);
    }

    private static void updateTopics() {
        flash.getManager().setTopic(pullFlash()).queue();
        arrow.getManager().setTopic(pullArrow()).queue();
        supergirl.getManager().setTopic(pullSupergirl()).queue();
        legends.getManager().setTopic(pullLegends()).queue();
    }

    private static String pullFlash() {
        if (flashData == null) return getTopic("the-flash");
        else return topicFromData(flashData);
    }

    private static String pullArrow() {
        if (arrowData == null) return getTopic("arrow");
        else return topicFromData(arrowData);
    }

    private static String pullSupergirl() {
        if (supergirlData == null) return getTopic("supergirl");
        else return topicFromData(supergirlData);
    }

    private static String pullLegends() {
        if (legendsData == null) return getTopic("legends-of-tomorrow");
        else return topicFromData(legendsData);
    }

    private static String getTopic(String show) {
        JSONObject episodeData = getLatestEpisode(show);
        if (episodeData == null) return null;

        saveData(show, episodeData);
        startPullTimer(show, episodeData);

        return topicFromData(episodeData);
    }

    private static void startPullTimer(String show, JSONObject episodeData) {
        try {
            Instant instant = toInstant(episodeData);
            long remaining = (instant.getEpochSecond() - Instant.now().getEpochSecond()) * 1000;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        JSONObject newData = getLatestEpisode(show);
                        Instant newTime = toInstant(newData);
                        if (newTime.getEpochSecond() > instant.getEpochSecond()) saveData(show, newData);
                        updateTopics();
                    } catch (Exception e) {
                        ExceptionHandler.handleException(e);
                    }
                }
            }, remaining + 60000, 600000);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private static void saveData(String show, JSONObject episodeData) {
        switch (show) {
            case "the-flash":
                flashData = episodeData;
                break;
            case "arrow":
                arrowData = episodeData;
                break;
            case "supergirl":
                supergirlData = episodeData;
                break;
            case "legends-of-tomorrow":
                legendsData = episodeData;
        }
    }

    private static String topicFromData(JSONObject episodeData) {
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

    private static JSONObject getLatestEpisode(String show) {
        JSONObject showData = readJsonFromUrl("http://api.tvmaze.com/search/shows?q=" + show);
        if (showData == null) return null;
        showData = showData.getJSONObject("show");

        String episodeUrl = showData.getJSONObject("_links").getJSONObject("nextepisode").getString("href");
        return readJsonFromUrl(episodeUrl);
    }

    private static Instant toInstant(String[] date, String[] time) throws Exception {
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        return ZonedDateTime.of(LocalDateTime.of(year, month, day, hour, minute), ZoneId.of("America/New_York")).toInstant();
    }

    private static Instant toInstant(JSONObject episodeData) throws Exception {
        String[] date = episodeData.getString("airdate").split("-");
        String[] time = episodeData.getString("airtime").split(":");
        return toInstant(date, time);
    }

    private static String timeRemaining(Instant instant) {
        Instant now = Instant.now();
        long remaining = instant.getEpochSecond() - now.getEpochSecond();

        long days = TimeUnit.SECONDS.toDays(remaining);
        remaining -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS.toHours(remaining);
        remaining -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = TimeUnit.SECONDS.toMinutes(remaining);

        return days + "d " + hours + "h " + minutes + "m";
    }

    private static JSONObject readJsonFromUrl(String url) {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            if (jsonText.startsWith("[")) return new JSONArray(jsonText).getJSONObject(0);
            else return new JSONObject(jsonText);
        } catch (Exception e) {
            ZLogger.warn("Could not get JSON from URL!");
            ExceptionHandler.handleException(e);
            return null;
        } finally {
            closeInputstream(is);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static void closeInputstream(InputStream is) {
        try {
            is.close();
        } catch (Exception e) {
            ZLogger.warn("Could not close inputstream!");
        }
    }

}
