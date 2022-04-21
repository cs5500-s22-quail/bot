package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import java.util.ArrayList;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;

public class AllPokemonController {
//    @Inject PokemonGenerator pokemonGenerator;
    @Inject UserPokemonController userPokemonController;

    @Inject
    public AllPokemonController() {}

    public EmbedBuilder getAllPokemonEmbeds(String userId) {
        UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberID(userId);
        ArrayList<PokemonInfo> userPokemonTeam = userPokemon.getPokemonTeam();
        String list = "";
        for (PokemonInfo pokemonInfo : userPokemonTeam) {
            list += "Name: " + pokemonInfo.getName() + " | ";
            list += "Level: " + pokemonInfo.getLevel() + " | ";
            list += "IV: " + pokemonInfo.getIv().getIVPercentageFormat() + "\n";
        }
        EmbedBuilder embedBuilder = new EmbedBuilder()；
        if (list.equals("")) {
            embedBuilder.setTitle("Ops, you haven't got a Pokemon yet.").setDescription("Try to search and catch one by: /search");
        }
        embedBuilder.setTitle("Your pokemon:").setDescription(list);
        return embedBuilder;
    }
}
