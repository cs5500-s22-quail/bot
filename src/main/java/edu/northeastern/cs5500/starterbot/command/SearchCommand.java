package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class SearchCommand implements Command {
    @Inject WildPokemonController wildPokemonController;

    @Inject
    public SearchCommand() {}

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public CommandData getCommandData() {
        // /search
        return new CommandData(getName(), "Search nearby wild pokemon")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "The bot will reply to your command with the provided text")
                                .setRequired(true));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /search");

        event.replyEmbeds(wildPokemonController.getOneRandomPokemon().build()).queue();
    }
}
