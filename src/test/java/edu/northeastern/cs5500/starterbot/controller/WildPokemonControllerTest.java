package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.PokemonIV;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WildPokemonControllerTest {

    WildPokemonController wildPokemonController;

    @BeforeEach
    void setUp() {
        this.wildPokemonController = new WildPokemonController(new InMemoryRepository<>());
        this.wildPokemonController.setRandomPokemonID();
    }

    @Test
    void setRandomPokemonID() {
        this.wildPokemonController.setRandomPokemonID();
        assertNotNull(this.wildPokemonController.getRandomPokemonID());
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
        PokemonIV pokemonIV = wildPokemon.getPokemonIV();
        assertNotNull(pokemonIV);
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

        assertNull(this.wildPokemonController.getWildPokemonForChannel(testChannel).getPokemonIV());
        this.wildPokemonController.updateWildPokemonForChannel(testChannel);
        assertNotNull(
                this.wildPokemonController.getWildPokemonForChannel(testChannel).getPokemonIV());
    }
}
