package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;

@Data
public class WildPokemonController {
    // maximum id exists in the pokemon API
    private static final int MAX_POKEMON_ID = 898;

    GenericRepository<WildPokemon> wildPokemonRepository;
    Integer randomPokemonID;

    @Inject
    public WildPokemonController(GenericRepository<WildPokemon> wildPokemonRepository) {
        this.wildPokemonRepository = wildPokemonRepository;
        this.setRandomPokemonID();
    }

    public void setRandomPokemonID() {
        Random rand = new Random();
        this.randomPokemonID = rand.nextInt(MAX_POKEMON_ID + 1);
    }

    @Nonnull
    public EmbedBuilder wildPokemonUserInterface() {

        ExtendedPokemon extendedPokemon = ExtendedPokemon.fromID(this.randomPokemonID);
        String officialArtworkUrl = extendedPokemon.getOfficialArtworkUrl();
        return new EmbedBuilder()
                .setTitle("A wild pokemon has appeared!")
                .setDescription(
                        "Guess the pokemon and type /catch <pokemon> to catch the pokemon."
                                + extendedPokemon.getSpeciesName())
                .setImage(officialArtworkUrl);
    }

    public void updateWildPokemonForChannel(String discordChannelID) {

        String speciesName = ExtendedPokemon.fromID(this.randomPokemonID).getSpeciesName();

        WildPokemon wildPokemon = this.getWildPokemonForChannel(discordChannelID);
        wildPokemon.setWildPokemonName(speciesName);
        wildPokemonRepository.update(wildPokemon);
    }

    @Nonnull
    private WildPokemon getWildPokemonForChannel(String discordChannelID) {
        Collection<WildPokemon> wildPokemons = wildPokemonRepository.getAll();
        for (WildPokemon wildPokemon : wildPokemons) {
            if (wildPokemon.getDiscordChannel().equals(discordChannelID)) return wildPokemon;
        }

        WildPokemon wildPokemon = new WildPokemon();
        wildPokemon.setDiscordChannel(discordChannelID);
        wildPokemonRepository.add(wildPokemon);
        return wildPokemon;
    }
}
