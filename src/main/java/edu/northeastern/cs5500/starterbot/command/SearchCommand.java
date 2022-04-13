package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.ExtendedPokemon;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
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
        ExtendedPokemon extendedPokemon = wildPokemonController.setRandomPokemonID();
        wildPokemonController.updateWildPokemonForChannel(channelID);

        String officialArtworkUrl = extendedPokemon.getOfficialArtworkUrl();
        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(channelID);

        int hp = 5;

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("A wild pokemon has appeared!")
                        .setDescription(
                                "Guess the pokemon and type /catch <pokemon> to catch the pokemon."
                                        + extendedPokemon.getSpeciesName()
                                        + "\n"
                                        + ivUI(
                                                "HP",
                                                extendedPokemon.getStatHp(),
                                                wildPokemon.getPokemonIV().getHp())
                                        + ivUI(
                                                "Attack",
                                                extendedPokemon.getStatAttack(),
                                                wildPokemon.getPokemonIV().getAttack())
                                        + ivUI(
                                                "Defense",
                                                extendedPokemon.getStatDefense(),
                                                wildPokemon.getPokemonIV().getDefense())
                                        + ivUI(
                                                "Sp.Atk",
                                                extendedPokemon.getStatSpecialAttack(),
                                                wildPokemon.getPokemonIV().getSpecialAttack())
                                        + ivUI(
                                                "Sp.Def",
                                                extendedPokemon.getStatSpecialDefense(),
                                                wildPokemon.getPokemonIV().getSpecialDefense())
                                        + ivUI(
                                                "Speed",
                                                extendedPokemon.getStatSpeed(),
                                                wildPokemon.getPokemonIV().getSpeed())
                                        + "\nTotal IV: "
                                        + wildPokemon.getPokemonIV().getIVPercentageFormat())
                        .setImage(officialArtworkUrl);

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private String ivUI(String statName, int baseStat, int iv) {
        return "\n" + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
