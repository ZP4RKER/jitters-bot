package com.zp4rker.bots.jitters.cmd;

import com.zp4rker.core.discord.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Assign {

    @Command(
            aliases = "assign"
    )
    public void onCommand(Message message, String[] args) {
        if (args.length < 1) {
            sendHelp(message);
        } else {
            String role = String.join("", args);
            switch (role.toLowerCase()) {
                case "theflash":
                case "flash":
                    role = "312572739808526336";
                    break;
                case "arrow":
                    role = "312572948856832000";
                    break;
                case "legendsoftomorrow":
                case "legends":
                    role = "312573020244017153";
                    break;
                case "supergirl":
                    role = "312573207632936972";
                    break;
                case "blacklightning":
                    role = "404616536133861377";
                    break;
                case "krypton":
                    role = "478839012002496512";
                    break;
                case "gotham":
                    role = "478839228747481098";
                    break;
                case "youngjustice":
                    role = "478839334288621578";
                    break;
                default: role = null;
            }

            Guild guild = message.getGuild();

            if (role == null) sendError(message);
            else if (message.getMember().getRoles().contains(guild.getRoleById(role))) sendWarning(message);
            else {
                guild.getController().addRolesToMember(message.getMember(), guild.getRoleById(role)).queue();
                sendConfirmation(message, guild.getRoleById(role));
            }
        }
        
        message.delete().complete();
    }

    private void sendHelp(Message message) {
        String[] roles = {
                "The Flash",
                "Arrow",
                "Legends of Tomorrow",
                "Supergirl",
                "Black Lightning",
                "Krypton",
                "Gotham",
                "Young Justice"
        };

        message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription("**Roles:**\n- " + String.join("\n- ", roles) + "\n\n" +
                        "**Usage:**\n`!assign <role>`")
                .setColor(Color.decode("#34c6f2")).build()).queue();
    }

    private void sendConfirmation(Message message, Role role) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription(":white_check_mark: You have assigned the `" + role.getName() + "` role to yourself!")
                .setColor(role.getColor()).build()).complete(), TimeUnit.SECONDS.toMillis(10));
    }

    private void sendError(Message message) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription(":x: That role doesn't exist or can't be self-assigned!")
                .setColor(Color.RED).build()).complete(), TimeUnit.SECONDS.toMillis(10));
    }

    private void sendWarning(Message message) {
        selfDestruct(message.getTextChannel().sendMessage(new EmbedBuilder()
                .setDescription(":warning: You already have that role!")
                .setColor(Color.YELLOW).build()).complete(), TimeUnit.SECONDS.toMillis(10));
    }

    private void selfDestruct(Message message, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                message.delete().complete();
            }
        }, delay);
    }

}
