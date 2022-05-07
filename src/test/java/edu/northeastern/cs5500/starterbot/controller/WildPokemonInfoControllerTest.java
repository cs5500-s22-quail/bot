package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WildPokemonInfoControllerTest {

    WildPokemonController wildPokemonController;

    @BeforeEach
    void setUp() {

        this.wildPokemonController = new WildPokemonController(new InMemoryRepository<>());
        this.wildPokemonController.pokemonService = new PokemonService();
    }

    @Test
    void updateWildPokemonForChannel() {
        String testChannel = "1234";
        this.wildPokemonController.updateWildPokemonForChannel(testChannel);
        Collection<WildPokemon> wildPokemons =
                this.wildPokemonController.wildPokemonRepository.getAll();
        WildPokemon wildPokemon = null;
        for (WildPokemon wp : wildPokemons) {
            if (wp.getDiscordChannel().equals(testChannel)) wildPokemon = wp;
        }
        PokemonInfo pokemonIV = wildPokemon.getPokemonInfo();

        assertThat(pokemonIV).isNotNull();
    }

    @Test
    void getWildPokemonForChannel() {
        String testChannel = "5678";
        Collection<WildPokemon> wildPokemons =
                this.wildPokemonController.wildPokemonRepository.getAll();
        for (WildPokemon wildPokemon : wildPokemons) {
            if (wildPokemon.getDiscordChannel().equals(testChannel))
                this.wildPokemonController.wildPokemonRepository.delete(wildPokemon.getId());
        }

        assertThat(
                        this.wildPokemonController
                                .getWildPokemonForChannel(testChannel)
                                .getPokemonInfo())
                .isNull();

        this.wildPokemonController.updateWildPokemonForChannel(testChannel);

        assertThat(
                        this.wildPokemonController
                                .getWildPokemonForChannel(testChannel)
                                .getPokemonInfo())
                .isNotNull();
    }

    @Test
    void testHasPokemonForChannel() {
        this.wildPokemonController.hasWildPokemonForChannel("0");
    }

    @Test
    void testDeletePokemon() {
        this.wildPokemonController.deletePokemonInfoForChannel("0");
    }
}
