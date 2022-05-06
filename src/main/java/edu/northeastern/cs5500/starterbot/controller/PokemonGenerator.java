package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PokemonGenerator {
    @Inject PokemonService pokemonService;
    private static final int MAX_POKEMON_ID = 898;

    @Inject
    public PokemonGenerator(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    // get a random wildPokemon with random level
    public WildPokemon getWildPokemon() {
        WildPokemon wildPokemon = new WildPokemon();
        PokemonInfo pokemonInfo =
                pokemonService.fromIDWithRandomLevel((int) (Math.random() * MAX_POKEMON_ID) + 1);
        wildPokemon.setPokemonInfo(pokemonInfo);
        return wildPokemon;
    }
}
