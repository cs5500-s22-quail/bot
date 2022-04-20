package edu.northeastern.cs5500.starterbot.controller;

import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;

public class ProfileController {

    @Inject
    public ProfileController() {}

    public EmbedBuilder getProfile(
            String discordUserId, String discordAvatarUrl, String preferredName) {
        int balance = 0; // the balance, initially set to 0. TBD
        int pokemons = 0; // the captured pokemons, initially set to 0. TBD
        int released = 0; // the released pokemons, initially set to 0. TBD
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle(preferredName + "'s Profile")
                        .setDescription(
                                "Balance: "
                                        + String.valueOf(balance)
                                        + "\nCaptured Pokemons: "
                                        + String.valueOf(pokemons)
                                        + "\nReleased Pokemons: "
                                        + String.valueOf(released)
                                        + "\nTotal Pokemons: "
                                        + String.valueOf(pokemons + released));

        return eb.setThumbnail(discordAvatarUrl);
    }
}
