package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import eu.iamgio.pokedex.exception.PokedexException;
import org.junit.jupiter.api.Test;

class PokemonServiceInfoTest {
    private static final int MAX_POKEMON_ID = 898;

    PokemonService pokemonService = new PokemonService();

    @Test
    void constructorFromIDTest() {

        assertThrows(PokedexException.class, () -> pokemonService.fromID(MAX_POKEMON_ID + 1));
    }

    @Test
    void getOfficialArtworkUrl() {
        assertThat(
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png")
                .isEqualTo(pokemonService.fromID(4).getOfficialArtworkUrl());
    }

    @Test
    void getSpeciesName() {

        assertThat("scyther").isEqualTo(pokemonService.fromID(123).getName());
    }

    @Test
    void getStat() {
        PokemonInfo extendedPokemon = pokemonService.fromName("ditto");

        assertThat(48).isEqualTo(extendedPokemon.getHp());
        assertThat(48).isEqualTo(extendedPokemon.getAttack());
        assertThat(48).isEqualTo(extendedPokemon.getDefense());
        assertThat(48).isEqualTo(extendedPokemon.getSpeed());
        assertThat(48).isEqualTo(extendedPokemon.getSpecialAttack());
        assertThat(48).isEqualTo(extendedPokemon.getSpecialDefense());
    }
}
