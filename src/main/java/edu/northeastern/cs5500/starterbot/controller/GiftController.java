package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import java.util.ArrayList;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

public class GiftController {

    @Inject UserPokemonController userPokemonController;

    @Inject
    public GiftController() {}

    public MessageBuilder getPokemonList(String discordUserId, String userName, String receiver) {
        UserPokemon userPokemon =
                userPokemonController.getUserPokemonForMemberID(discordUserId, userName);

        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Please choose one your pokemons to gift to ." + receiver)
                .setDescription(
                        "\n\nPlease note that you can not give your carried pokemon to others");
        eb.setImage("https://c.tenor.com/JdW7qW5GGMcAAAAM/christmas-pokemon.gif");
        ArrayList<PokemonInfo> pokemons = userPokemon.getPokemonTeam();
        ArrayList<SelectOption> nameOptions = new ArrayList<>();
        for (PokemonInfo pokemon : pokemons) {
            // could not give carried pokemon to others.
            if (pokemon.getName().equals(userPokemon.getCarriedPokemon().getName())) continue;
            String currentName = pokemon.getName();
            nameOptions.add(SelectOption.of(currentName, currentName));
        }

        if (nameOptions.size() == 0) {
            eb.setTitle("Sorry You do not have any pokemons to give");
            mb.setEmbeds(eb.build());
            return mb;
        }

        SelectionMenu menu =
                SelectionMenu.create("pokemon-list")
                        .setPlaceholder("Please choose from the following pokemons to give\n")
                        .addOptions(nameOptions)
                        .build();
        mb.setEmbeds(eb.build()).setActionRows(ActionRow.of(menu));

        return mb;
    }


}
