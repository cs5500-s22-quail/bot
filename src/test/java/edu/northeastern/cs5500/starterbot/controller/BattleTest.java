package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    @Test
    void pokemonVersePokemon() {
        Battle battle = new Battle();
        PokemonService pokemonService = new PokemonService();
        Object[] info = battle.pokemonVersePokemon(pokemonService.fromID(30), pokemonService.fromID(100));
        System.out.println(info[1]);
    }
}