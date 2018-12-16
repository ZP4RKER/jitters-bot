package com.zp4rker.bots.jitters.cmd;

import com.zp4rker.core.discord.command.Command;
import com.zp4rker.bots.jitters.Jitters;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class Info {

    @Command(
            aliases = "info"
    )
    public void onCommand(Message message) {
        MessageEmbed embed = new EmbedBuilder()
        .setAuthor("Jitters Bot", null, message.getJDA().getSelfUser().getEffectiveAvatarUrl())
        .addField("Author", "ZP4RKER#3333", true)
        .addField("Version", Jitters.VERSION, true)
        .addField("Commands", "" + Jitters.handler.getCommands().size(), true)
        .addField("Description", "The official Jitters bot", true)
        .build();

        message.getChannel().sendMessage(embed).queue();
    }

}
