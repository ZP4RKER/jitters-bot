package me.zp4rker.discord.jitters.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ZP4RKER
 */
public class SAssignCommand implements ICommand {

    @RegisterCommand(aliases = {"sassign", "assign"},
                    description = "Assigns a role to the user.",
                    usage = "{prefix}sassign role")
    public void onCommand(Message message, String[] args) {
        if (args.length < 1) {
            sendHelp(message);
        } else {
            String role = String.join("", args);
            switch (role.toUpperCase()) {
                case "THEFLASH":
                case "FLASH":
                    role = "312572739808526336";
                    break;
                case "ARROW":
                    role = "312572948856832000";
                    break;
                case "LEGENDSOFTOMORROW":
                case "LEGENDS":
                    role = "312573020244017153";
                    break;
                case "SUPERGIRL":
                    role = "312573207632936972";
                    break;
                default: role = null;
            }

            Guild guild = message.getGuild();

            if (role == null) sendError(message);
            else if (message.getMember().getRoles().contains(guild.getRoleById(role))) sendWarning(message);
            else {
                guild.getController().addRolesToMember(message.getMember(), guild.getRoleById(role)).complete();
                sendConfirmation(message, guild.getRoleById(role));
            }
        }
        
        delete(message);
    }

    private void sendHelp(Message message) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription("**Roles:**\n- The Flash\n- Arrow\n- Legends of Tomorrow\n- Supergirl\n\n" +
                        "**Usage:**\n`!sassign <role>`")
                .setColor(Color.decode("#34c6f2")).build()).complete(), 15000);
    }

    private void sendConfirmation(Message message, Role role) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription(":white_check_mark: You have assigned the `" + role.getName() + "` role to yourself!")
                .setColor(role.getColor()).build()).complete(), 7000);
    }

    private void sendError(Message message) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription(":x: That role doesn't exist!")
                .setColor(Color.RED).build()).complete(), 6000);
    }

    private void sendWarning(Message message) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription(":warning: You already have that role!")
                .setColor(Color.YELLOW).build()).complete(), 6000);
    }

    private void selfDestruct(Message message, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                message.delete().complete();
            }
        }, delay);
    }

    private void delete(Message message) {
        Message msg = message.getChannel().sendMessage("`").complete();
        message.getTextChannel().deleteMessages(Arrays.asList(message, msg)).complete();
    }

}
