package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.Data;

@Data
public class WildPokemonController {
    @Inject PokemonService pokemonService;

    private static final int MAX_POKEMON_ID = 898;

    GenericRepository<WildPokemon> wildPokemonRepository;
    Random rand;
    Integer randomPokemonID;

    @Inject
    public WildPokemonController(GenericRepository<WildPokemon> wildPokemonRepository) {
        this.wildPokemonRepository = wildPokemonRepository;
        this.rand = new Random();
        this.randomPokemonID = this.rand.nextInt(MAX_POKEMON_ID + 1);
    }

    public void updateWildPokemonForChannel(String discordChannelID) {
        this.randomPokemonID = this.rand.nextInt(MAX_POKEMON_ID + 1);

        PokemonInfo pokemonInfo = pokemonService.fromID(this.randomPokemonID);

        WildPokemon wildPokemon = this.getWildPokemonForChannel(discordChannelID);

        wildPokemon.setPokemonInfo(pokemonInfo);

        wildPokemonRepository.update(wildPokemon);
    }

    @Nonnull
    public WildPokemon getWildPokemonForChannel(String discordChannelID) {
        Collection<WildPokemon> wildPokemons = wildPokemonRepository.getAll();
        for (WildPokemon wildPokemon : wildPokemons) {
            if (wildPokemon.getDiscordChannel().equals(discordChannelID)) return wildPokemon;
        }

        WildPokemon wildPokemon = new WildPokemon();
        wildPokemon.setDiscordChannel(discordChannelID);

        wildPokemonRepository.add(wildPokemon);
        return wildPokemon;
    }

    @Nonnull
    public Boolean hasWildPokemonForChannel(String discordChannelID) {
        return this.getWildPokemonForChannel(discordChannelID).getPokemonInfo() != null;
    }
}
