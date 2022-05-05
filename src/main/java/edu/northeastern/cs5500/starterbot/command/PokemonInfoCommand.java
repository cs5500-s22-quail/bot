package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokemonInfoController;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class PokemonInfoCommand implements Command {

    @Inject UserPokemonController userPokemonController;

    @Inject
    public PokemonInfoCommand() {}

    @Override
    public String getName() {
        return "pokemoninfo";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Ask the bot to reply with the carried pokemon info");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /pokemoninfo");
        UserPokemon userPokemon =
                userPokemonController.getUserPokemonForMemberId(event.getUser().getId());
        PokemonInfo userPokeInfo = userPokemon.getCarriedPokemon();
        PokemonInfoController pokemonInfoController = new PokemonInfoController(userPokeInfo);
        event.replyEmbeds(pokemonInfoController.getPokemonInfoEmbed().build()).queue();
    }
}
