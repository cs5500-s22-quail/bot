package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.Test;

class BattleTest {

    @Test
    void pokemonVersePokemon() {
        Battle battle = new Battle();
        PokemonService pokemonService = new PokemonService();
        Object[] info =
                battle.pokemonVersePokemon(pokemonService.fromID(30), pokemonService.fromID(100));
    }
}
