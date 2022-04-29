package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.Test;

class DisplayControllerTest {

    @Test
    void pokemonInfoUI() {
        DisplayController displayController = new DisplayController();
        PokemonService pokemonService = new PokemonService();
        assertNotNull(displayController.PokemonInfoUI(pokemonService.fromID(12)));
    }

    @Test
    void pokemonStatus() {
        DisplayController displayController = new DisplayController();
        PokemonService pokemonService = new PokemonService();
        assertNotNull(displayController.pokemonStatus(pokemonService.fromID(12)));
    }
}
