package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.SelectCommandController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class SelectCommand implements Command {

    @Inject SelectCommandController selectCommandController;

    @Inject
    public SelectCommand() {}

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Select the pokemon from your all-Pokemon list")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "Please enter the index of your selected Pokemon")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /select");
        //        event.reply(event.getOption("content").getAsString()).queue();
        String userId = event.getUser().getId();
        String userSelect = event.getOption("content").getAsString();
        event.replyEmbeds(selectCommandController.getSelectEmbeds(userId, userSelect).build())
                .queue();
    }
}
