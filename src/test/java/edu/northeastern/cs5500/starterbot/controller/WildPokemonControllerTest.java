package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import net.dv8tion.jda.api.EmbedBuilder;
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
    void wildPokemonUserInterface() {
        EmbedBuilder embedBuilder = this.wildPokemonController.wildPokemonUserInterface();
    }

    @Test
    void updateWildPokemonForChannel() {}
}
