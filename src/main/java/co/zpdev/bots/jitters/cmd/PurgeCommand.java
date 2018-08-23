package co.zpdev.bots.jitters.cmd;

//TODO: Rewrite class
public class PurgeCommand {

    /*private int count;

    @Command(
            aliases = "purge",
            usage = "!purge {@user} {#}",
            permission = Permission.MESSAGE_MANAGE,
            minArgs = 1
    )
    public void onCommand(Message message, String[] args) {
        User user = null;
        if (message.getMentionedUsers().size() == 1) user = message.getMentionedUsers().get(0);

        int amount;
        try {
            if (user == null) amount = Integer.parseInt(args[0]);
            else amount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            MessageUtil.sendError("Invalid Arguments", "You provide valid arguments!" +
                    "\nCorrect usage: `sq!purge [user] {#}`", message);
            return;
        }

        MessageHistory history = message.getTextChannel().getHistory();

        List<Message> messages = new ArrayList<>();

        if (amount > 99) {
            messages.addAll(history.retrievePast(100).complete());
            while (amount > messages.size()) {
                if (((amount + 1) - messages.size()) > 100) messages.addAll(history.retrievePast(100).complete());
                else messages.addAll(history.retrievePast((amount + 1) - messages.size()).complete());
            }
        } else messages.addAll(history.retrievePast(amount + 1).complete());

        List<Message> toDelete = new ArrayList<>();

        if (user == null) toDelete.addAll(messages);
        else {
            messages.addAll(history.retrievePast(100).complete());
            messages.addAll(history.retrievePast(100).complete());
            messages.addAll(history.retrievePast(100).complete());
            final String id = user.getId();
            count = 0;
            messages.forEach(m -> {
                if (count < amount) {
                    if (m.getAuthor().getId().equals(id)) toDelete.add(m);
                    count++;
                }
            });
        }

        message.getTextChannel().deleteMessages(toDelete).queue();
        sendNotification(message, toDelete.size() - 1);
        sendLog(message.getMember(), message.getTextChannel(), toDelete.size() - 1);
    }

    private void sendLog(Member member, TextChannel channel, int amount) {
        // Log channel = 314654582183821312L
        User user = member.getUser();

        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("Messages purged", null, member.getGuild().getIconUrl())
                .setDescription(channel.getAsMention() + " purged by " + user.getAsMention() + ".\n" +
                        "Messages: " + amount + "\n")
                .setColor(Color.ORANGE)
                .setTimestamp(Instant.now()).build();

        member.getGuild().getTextChannelById(314654582183821312L).sendMessage(embed).queue();
    }

    private void sendNotification(Message message, int amount) {
        MessageEmbed embed = new EmbedBuilder()
                .setDescription(message.getAuthor().getAsMention() + " just purged " + amount + " messages.")
                .setColor(Color.ORANGE).build();
        message.getChannel().sendMessage(embed).queue(m -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                m.delete().complete();
            }
        }, 10000));
    }*/

}
