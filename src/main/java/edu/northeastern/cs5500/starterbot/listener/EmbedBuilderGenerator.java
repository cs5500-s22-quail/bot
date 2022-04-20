package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedBuilderGenerator {
    @Inject PokemonGenerator pokemonGenerator;

    @Inject
    public EmbedBuilderGenerator() {}

    public EmbedBuilder getFightPokemonEmbeds() {
        WildPokemon fightPokemon = pokemonGenerator.getWildPokemon();
        PokemonInfo fightPokemonInfo = fightPokemon.getPokemonInfo();
        String officialArtworkUrl = fightPokemonInfo.getOfficialArtworkUrl();

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle(fightPokemonInfo.getName() + " is fighting with your pokemon...")
                        .setDescription(
                                // TODO: change attributes for /fight usage
                                //                                        +
                                // ivUIBundle(fightPokemonInfo)
                                "HP: "
                                        + fightPokemonInfo.getHp()
                                        + " | Level: "
                                        + fightPokemonInfo.getLevel())
                        .setImage(officialArtworkUrl);
        return embedBuilder;
    }

    private String ivUI(String statName, int baseStat, int iv) {
        return "\n" + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
