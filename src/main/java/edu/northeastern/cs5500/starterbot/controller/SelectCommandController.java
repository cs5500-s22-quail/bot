package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.EmbedBuilder;

@Singleton
public class SelectCommandController {

    @Inject UserPokemonController userPokemonController;

    @Inject
    public SelectCommandController() {}

    public EmbedBuilder getSelectEmbeds(String userId, String userSelect) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        int userSelectIndex;
        try {
            userSelectIndex = Integer.valueOf(userSelect);
        } catch (Exception e) {
            embedBuilder.setTitle("Invalid input! Please enter an integer.");
            return embedBuilder;
        }
        UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberID(userId);
        ArrayList<PokemonInfo> userPokemonTeam = userPokemon.getPokemonTeam();
        int maxIndex = userPokemonTeam.size();
        if (maxIndex == 0) {
            embedBuilder
                    .setTitle("Ops, you haven't got a Pokemon yet.")
                    .setDescription("Try to search and catch one by: /search");
            return embedBuilder;
        } else if (userSelectIndex <= 0 || userSelectIndex > maxIndex) {
            embedBuilder.setTitle(
                    "Out of range input! Please check your pokemon list by: /allpokemon");
            return embedBuilder;
        }
        PokemonInfo userSelectedPokemonInfo = userPokemonTeam.get(userSelectIndex - 1);
        userPokemon.setCarriedPokemon(userSelectedPokemonInfo);
        embedBuilder.setTitle(
                "You selected your Level "
                        + userSelectedPokemonInfo.getLevel()
                        + " "
                        + userSelectedPokemonInfo.getName());
        userPokemonController.userPokemonRepository.update(userPokemon);
        return embedBuilder;
    }
}
