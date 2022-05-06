package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PokemonInfoControllerTest {

    PokemonService pokemonService;
    PokemonInfoController pokemonInfoController;

    @BeforeEach
    void setUp() {
        pokemonService = new PokemonService();
        pokemonInfoController = new PokemonInfoController(pokemonService.fromID(10));
    }

    @Test
    void getPokemonInfoEmbed() {
        assertThat(pokemonInfoController.getPokemonInfoEmbed()).isNotNull();
    }
}
