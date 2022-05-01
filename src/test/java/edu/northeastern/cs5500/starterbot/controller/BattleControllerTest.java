package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.Test;

class BattleControllerTest {

    @Test
    void pokemonVersePokemon() {
        BattleController battleController = new BattleController();
        PokemonService pokemonService = new PokemonService();
        Object[] info =
                battleController.pokemonVersusPokemon(
                        pokemonService.fromID(30), pokemonService.fromID(100));
    }
}
