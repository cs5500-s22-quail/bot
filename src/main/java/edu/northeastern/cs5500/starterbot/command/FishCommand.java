package edu.northeastern.cs5500.starterbot.command;

import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class FishCommand implements Command {

    @Inject
    public FishCommand() {}

    @Override
    public String getName() {
        return "fish";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Ask the bot to go fishing");
        // .addOptions(
        //         new OptionData(
        //                         OptionType.STRING,
        //                         "content",
        //                         "The bot will go fishing and bring something back")
        //                 .setRequired(false));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /fish");
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("You threw your fishing rod...");
        eb.setImage(
                "https://cdn.discordapp.com/attachments/890889580021157918/928700605311119380/e8ef393b431d0a4ce738f13af0e9d022.gif");
        event.replyEmbeds(eb.build()).queue();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        Random rand = new Random();
        int gotCredit = rand.nextInt(11) * 10;
        eb.setTitle(":moneybag: You got " + gotCredit + " credits!");
        eb.setImage(null);
        event.getHook().editOriginalEmbeds(eb.build()).queue();
    }
}
