package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.jitters.Jitters;
import me.zp4rker.discord.core.exception.ExceptionHandler;
import me.zp4rker.discord.jitters.util.JSONUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * @author ZP4RKER
 */
public class MessageListener {

    @SubscribeEvent
    public void onMessage(GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Message message = event.getMessage();

        if (channel.getId().equals("314654582183821312")) return;
        if (event.getAuthor().isBot()) return;

        try {
            JSONObject file = JSONUtil.readFile(getFile(channel));
            JSONArray messageArray = file.has("messages") ? file.getJSONArray("messages") : new JSONArray();

            JSONObject data = new JSONObject();
            data.put("id", message.getId());
            data.put("author", message.getAuthor().getId());
            data.put("channel", message.getChannel().getId());
            data.put("creation-time", message.getCreationTime().toEpochSecond());
            data.put("content", message.getRawContent());

            if (messageArray.length() == 100) messageArray.remove(0);

            messageArray.put(data);

            file.put("messages", messageArray);
            JSONUtil.writeFile(file.toString(2), getFile(channel));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    static File getFile(TextChannel channel) throws Exception {
        File file = new File(Jitters.getDirectory(), "messages/" + channel.getId());
        if (!file.getParentFile().exists()) file.getParentFile().mkdir();
        if (!file.exists()) file.createNewFile();
        return file;
    }

}
