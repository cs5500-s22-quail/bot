package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class APIExtensionTest {

    @Test
    void getOfficialArtworkUrl() {
        ExtendedPokemon extendedPokemon = new ExtendedPokemon(4);
        assertEquals(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                extendedPokemon.getOfficialArtworkUrl());
    }
}
