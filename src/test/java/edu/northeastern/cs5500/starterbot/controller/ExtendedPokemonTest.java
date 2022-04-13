package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import eu.iamgio.pokedex.exception.PokedexException;
import org.junit.jupiter.api.Test;

class ExtendedPokemonTest {
    private static final int MAX_POKEMON_ID = 898;

    @Test
    void constructorFromIDTest() {
        assertNotNull(ExtendedPokemon.fromID(34).getPokemonJson());
        assertThrows(PokedexException.class, () -> ExtendedPokemon.fromID(MAX_POKEMON_ID + 1));
    }

    @Test
    void getOfficialArtworkUrl() {
        ExtendedPokemon extendedPokemon = ExtendedPokemon.fromID(4);
        assertEquals(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                extendedPokemon.getOfficialArtworkUrl());
    }

    @Test
    void getSpeciesName() {
        ExtendedPokemon extendedPokemon = ExtendedPokemon.fromID(123);
        assertEquals("scyther", extendedPokemon.getSpeciesName());
    }

    @Test
    void getStat() {
        ExtendedPokemon extendedPokemon = ExtendedPokemon.fromName("ditto");
        assertEquals(48, extendedPokemon.getStatHp());
        assertEquals(48, extendedPokemon.getStatAttack());
        assertEquals(48, extendedPokemon.getStatDefense());
        assertEquals(48, extendedPokemon.getStatSpeed());
        assertEquals(48, extendedPokemon.getStatSpecialAttack());
        assertEquals(48, extendedPokemon.getStatSpecialDefense());
    }
}
