package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import eu.iamgio.pokedex.exception.PokedexException;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

class PokemonServiceInfoTest {
    private static final int MAX_POKEMON_ID = 898;

    @Inject PokemonService pokemonService;

    @Test
    void constructorFromIDTest() {

        assertThrows(PokedexException.class, () -> pokemonService.fromID(MAX_POKEMON_ID + 1));
    }

    @Test
    void getOfficialArtworkUrl() {
        assertEquals(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                pokemonService.fromID(4).getOfficialArtworkUrl());
    }

    @Test
    void getSpeciesName() {

        assertEquals("scyther", pokemonService.fromID(123).getName());
    }

    @Test
    void getStat() {
        PokemonInfo extendedPokemon = pokemonService.fromName("ditto");
        assertEquals(48, extendedPokemon.getHp());
        assertEquals(48, extendedPokemon.getAttack());
        assertEquals(48, extendedPokemon.getDefense());
        assertEquals(48, extendedPokemon.getSpeed());
        assertEquals(48, extendedPokemon.getSpecialAttack());
        assertEquals(48, extendedPokemon.getSpecialDefense());
    }
}
