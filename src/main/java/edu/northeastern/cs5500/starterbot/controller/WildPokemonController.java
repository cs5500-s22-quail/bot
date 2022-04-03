package edu.northeastern.cs5500.starterbot.controller;

import java.util.Random;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;

public class WildPokemonController {
    private static final int MAX_POKEMON_NUMBER = 3;
    // maximum id exists in the pokemon API
    private static final int MAX_POKEMON_ID = 898;

    @Inject
    public WildPokemonController() {}

    public EmbedBuilder getOneRandomPokemon() {
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle("A wild pokemon has appeared!")
                        .setDescription(
                                "Guess the pokemon and type /catch <pokemon> to catch the pokemon");

        Random rand = new Random();
        int pokemonID = rand.nextInt(MAX_POKEMON_ID + 1);
        String officialArtworkUrl = new ExitendedPokemon(pokemonID).getOfficialArtworkUrl();

        return eb.setImage(officialArtworkUrl);
    }
}
