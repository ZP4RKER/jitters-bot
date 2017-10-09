package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.core.exception.ExceptionHandler;
import me.zp4rker.discord.jitters.util.JSONUtil;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author ZP4RKER
 */
public class BulkDeleteListener {

    @SubscribeEvent
    public void onBulkDelete(MessageBulkDeleteEvent event) {
        TextChannel c = event.getChannel();

        if (c == null) return;

        try {
            JSONObject file = JSONUtil.readFile(MessageListener.getFile(c));
            JSONArray messagesArray = file.getJSONArray("messages");

            for (String id : event.getMessageIds()) {
                int i = searchForMessage(messagesArray, id);
                if (i < 0) continue;

                messagesArray.remove(i);
            }

            file.put("messages", messagesArray);
            JSONUtil.writeFile(file.toString(2), MessageListener.getFile(c));
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

}
