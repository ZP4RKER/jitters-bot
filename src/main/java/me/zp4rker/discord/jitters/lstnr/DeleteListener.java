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

/**
 * @author ZP4RKER
 */
public class DeleteListener {

    @SubscribeEvent
    public void onDelete(GuildMessageDeleteEvent event) {
        TextChannel channel = event.getChannel();
        String id = event.getMessageId();

        try {
            JSONObject file = JSONUtil.readFile(MessageListener.getFile(channel));
            JSONArray messagesArray = file.getJSONArray("messages");

            JSONObject data = searchMessage(messagesArray, id);
            if (data == null) return;

            sendLog(data);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private JSONObject searchMessage(JSONArray array, String id) {
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString("id").equals(id)) return array.getJSONObject(i);
        }
        return null;
    }

    private void sendLog(JSONObject data) {
        User user = Jitters.jda.getUserById(data.getString("author"));
        TextChannel channel = Jitters.jda.getTextChannelById(data.getString("channel"));

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("**Message from " + user.getAsMention() + " deleted in **" + channel.getAsMention()
                                + "\n" + data.getString("content"))
                .setFooter("ID: " + data.getString("id"), null)
                .setTimestamp(Instant.now())
                .setColor(new Color(250, 166, 26)).build();

        Jitters.jda.getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

}
