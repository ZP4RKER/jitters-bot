package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.jitters.Jitters;
import me.zp4rker.discord.core.exception.ExceptionHandler;
import me.zp4rker.discord.jitters.util.JSONUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZP4RKER
 */
public class DeleteListener {

    public static List<String> bypass = new ArrayList<>();

    @SubscribeEvent
    public void onDelete(GuildMessageDeleteEvent event) {
        if (event.getChannel().getId().equals("314654582183821312")) return;

        TextChannel channel = event.getChannel();
        String id = event.getMessageId();

        try {
            JSONObject file = JSONUtil.readFile(MessageListener.getFile(channel));
            JSONArray messagesArray = file.getJSONArray("messages");

            int index = searchForMessage(messagesArray, id);
            if (index < 0) return;
            JSONObject data = messagesArray.getJSONObject(index);

            if (!bypass.contains(event.getMessageId())) sendLog(data);

            messagesArray.remove(index);
            file.put("messages", messagesArray);
            JSONUtil.writeFile(file.toString(2), MessageListener.getFile(channel));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private int searchForMessage(JSONArray array, String id) {
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString("id").equals(id)) return i;
        }
        return -1;
    }

    private void sendLog(JSONObject data) {
        User user = Jitters.jda.getUserById(data.getString("author"));
        if (user == null) return;

        TextChannel channel = Jitters.jda.getTextChannelById(data.getString("channel"));

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("**Message from " + user.getAsMention() + " deleted in **" + channel.getAsMention()
                                + "\n" + data.getString("content"))
                .setFooter("ID: " + data.getString("id"), null)
                .setTimestamp(Instant.now())
                .setColor(new Color(240, 71, 71)).build();

        Jitters.jda.getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
