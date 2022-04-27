package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.AllPokemonController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class AllPokemonCommand implements Command {

    @Inject
    public AllPokemonCommand() {}

    @Inject AllPokemonController embedBuilderGenerator;

    @Override
    public String getName() {
        return "allpokemon";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "List all caught pokemon ");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /allpokemon");
        event.replyEmbeds(
                        embedBuilderGenerator.getAllPokemonEmbeds(event.getUser().getId()).build())
                .queue();
    }
}
