package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.EmbedBuilder;

@Singleton
public class AllPokemonController {
    @Inject UserPokemonController userPokemonController;

    @Inject
    public AllPokemonController() {}

    public EmbedBuilder getAllPokemonEmbeds(String userId) {
        UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberId(userId);
        ArrayList<PokemonInfo> userPokemonTeam = userPokemon.getPokemonTeam();
        StringBuilder list = new StringBuilder();
        int index = 0;
        for (PokemonInfo pokemonInfo : userPokemonTeam) {
            index++;
            list.append(
                    String.format(
                            "%s - **%s** | Quality: %s | Level: %s | IV: %s \n",
                            index,
                            pokemonInfo.getName(),
                            pokemonInfo.getIv().getQualityName(),
                            pokemonInfo.getLevel(),
                            pokemonInfo.getIv().getIVPercentageFormat()));
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (index == 0) {
            embedBuilder
                    .setTitle("Ops, you haven't got a Pokemon yet.")
                    .setDescription("Try to search and catch one by: /search");
        } else {
            embedBuilder.setTitle("Your pokemon:").setDescription(list);
        }
        return embedBuilder;
    }
}
