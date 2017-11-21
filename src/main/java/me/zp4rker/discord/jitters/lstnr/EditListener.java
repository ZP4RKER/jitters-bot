package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.core.exception.ExceptionHandler;
import me.zp4rker.discord.jitters.util.JSONUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.time.Instant;

/**
 * @author ZP4RKER
 */
public class EditListener {

    @SubscribeEvent
    public void onMessageEdit(GuildMessageUpdateEvent event) {
        TextChannel channel = event.getChannel();
        String id = event.getMessageId();
        String content = event.getMessage().getRawContent();

        if (channel.getId().equals("314654582183821312")) return;
        if (event.getAuthor().isBot()) return;

        try {
            JSONObject file = JSONUtil.readFile(MessageListener.getFile(channel));
            JSONArray messageArray = file.has("messages") ? file.getJSONArray("messages") : new JSONArray();

            int index = getIndex(messageArray, id);
            if (index < 0) return;

            JSONObject data = messageArray.getJSONObject(index);
            sendLog(event.getMessage(), data.getString("content"));
            data.put("content", content);

            messageArray.put(index, data);
            file.put("messages", messageArray);
            JSONUtil.writeFile(file.toString(2), MessageListener.getFile(channel));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void sendLog(Message message, String oldContent) {
        String newContent = message.getRawContent();
        User user = message.getAuthor();
        TextChannel channel = message.getTextChannel();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setDescription("**Message from " + user.getAsMention() + " edited in **" + channel.getAsMention())
                .addField("Original", oldContent, false)
                .addField("New", newContent, false)
                .setFooter("ID: " + message.getId(), null)
                .setTimestamp(Instant.now())
                .setColor(Color.YELLOW).build();

        message.getJDA().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

    private int getIndex(JSONArray array, String id) {
        int index = -1;

        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString("id").equals(id)) index = i;
        }

        return index;
    }

}
