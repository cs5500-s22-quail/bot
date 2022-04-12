package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

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
        return new CommandData(getName(), "Search nearby wild pokemon");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /search");

        wildPokemonController.setRandomPokemonID();

        event.replyEmbeds(wildPokemonController.wildPokemonUserInterface().build()).queue();

        wildPokemonController.updateWildPokemonForChannel(event.getMessageChannel().getId());
    }
}
