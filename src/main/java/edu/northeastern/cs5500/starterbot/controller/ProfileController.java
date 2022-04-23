package edu.northeastern.cs5500.starterbot.controller;

import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;

public class ProfileController {

    @Inject ShopController shopController;
    @Inject UserPokemonController userPokemonController;

    @Inject
    public ProfileController() {}

    public EmbedBuilder getProfile(
            String discordUserId, String discordAvatarUrl, String preferredName) {
        int balance =
                shopController
                        .getBalanceForUserId(discordUserId)
                        .getBalance(); // the balance, initially set to 0. TBD
        int pokemons =
                userPokemonController
                        .getUserPokemonForMemberID(discordUserId)
                        .getPokemonTeam()
                        .size(); // the captured pokemons, initially set to 0. TBD
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle(preferredName + "'s Profile")
                        .setDescription(
                                "Balance: "
                                        + String.valueOf(balance)
                                        + "\nCaptured Pokemons: "
                                        + String.valueOf(pokemons))
                        .setThumbnail(discordAvatarUrl);

        return eb;
    }
}
