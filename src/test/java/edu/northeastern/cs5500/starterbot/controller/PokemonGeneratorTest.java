package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.Test;

class PokemonGeneratorTest {
    PokemonService pokemonService = new PokemonService();
    PokemonGenerator pokemonGenerator = new PokemonGenerator(pokemonService);

    @Test
    void getWildPokemon() {
        assertThat(this.pokemonGenerator.getWildPokemon()).isNotNull();
    }
}
