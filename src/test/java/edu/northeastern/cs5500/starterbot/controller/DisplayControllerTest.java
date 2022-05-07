package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.Test;

class DisplayControllerTest {

    @Test
    void pokemonInfoUI() {
        DisplayController displayController = new DisplayController();
        PokemonService pokemonService = new PokemonService();
        assertThat(displayController.PokemonInfoUI(pokemonService.fromID(12))).isNotNull();
    }

    @Test
    void pokemonStatus() {
        DisplayController displayController = new DisplayController();
        PokemonService pokemonService = new PokemonService();
        assertThat(displayController.pokemonStatus(pokemonService.fromID(12))).isNotNull();
    }
}
