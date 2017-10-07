package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.logger.ZLogger;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZP4RKER
 */
public class BulkDeleteListener implements ICommand {

    static List<String> bulkDeleted = new ArrayList<>();

    @SubscribeEvent
    public void onBulkDelete(MessageBulkDeleteEvent event) {
        if (event.getGuild() == null) return;
        bulkDeleted.addAll(event.getMessageIds());
        ZLogger.debug("Added messages to list.");
    }

}
