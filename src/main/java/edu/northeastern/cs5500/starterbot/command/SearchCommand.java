package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.Quality;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
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
        String channelID = event.getMessageChannel().getId();

        wildPokemonController.updateWildPokemonForChannel(channelID);

        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(channelID);

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("A wild pokemon has appeared!")
                        .setDescription(
                                "Guess the pokemon and type /catch <pokemon> to catch the pokemon."
                                        + wildPokemon.getPokemonInfo().getName()
                                        + System.lineSeparator()
                                        + ivUIBundle(wildPokemon.getPokemonInfo()))
                        .setImage(wildPokemon.getPokemonInfo().getOfficialArtworkUrl());

        Quality quality = wildPokemon.getPokemonInfo().getIv().getQuality();
        switch (quality) {
            case RED:
                embedBuilder = embedBuilder.setColor(0xff210d);
                break;
            case PURPLE:
                embedBuilder = embedBuilder.setColor(0xc30dff);
                break;
            case BLUE:
                embedBuilder = embedBuilder.setColor(0x0d7eff);
                break;
            case GREEN:
                embedBuilder = embedBuilder.setColor(0x0dff82);
                break;
            default:
                break;
        }
        event.replyEmbeds(embedBuilder.build()).queue();
    }

    public String ivUIBundle(PokemonInfo pokemonInfo) {
        return ivUI("HP", pokemonInfo.getHp(), pokemonInfo.getIv().getHp())
                + ivUI("Attack", pokemonInfo.getAttack(), pokemonInfo.getIv().getAttack())
                + ivUI("Defense", pokemonInfo.getDefense(), pokemonInfo.getIv().getDefense())
                + ivUI(
                        "Sp.Atk",
                        pokemonInfo.getSpecialAttack(),
                        pokemonInfo.getIv().getSpecialAttack())
                + ivUI(
                        "Sp.Def",
                        pokemonInfo.getSpecialDefense(),
                        pokemonInfo.getIv().getSpecialDefense())
                + ivUI("Speed", pokemonInfo.getSpeed(), pokemonInfo.getIv().getSpeed())
                + "\nTotal IV: "
                + pokemonInfo.getIv().getIVPercentageFormat();
    }

    public String ivUI(String statName, int baseStat, int iv) {
        return System.lineSeparator() + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
