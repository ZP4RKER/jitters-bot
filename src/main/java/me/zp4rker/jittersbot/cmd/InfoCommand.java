package me.zp4rker.jittersbot.cmd;

import me.zp4rker.core.command.ICommand;
import me.zp4rker.core.command.RegisterCommand;
import me.zp4rker.jittersbot.Jitters;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * @author ZP4RKER
 */
public class InfoCommand implements ICommand {

    @RegisterCommand(aliases = "info",
                    description = "Displays info about the bot.",
                    usage = "{prefix}info")
    public void onCommand(Message message) {
        message.getChannel().sendMessage(compileEmbed(message.getJDA())).complete();
    }

    private MessageEmbed compileEmbed(JDA jda) {
        return new EmbedBuilder()
                .setAuthor("Jitters Bot", null, jda.getSelfUser().getEffectiveAvatarUrl())
                .addField("Author", "ZP4RKER#3333", true)
                .addField("Version", Jitters.VERSION, true)
                .addField("Commands", "" + Jitters.handler.getCommands().size(), true)
                .addField("Description", "Lorem ipsum.", true)
                .build();
    }


}
