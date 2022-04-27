package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllPokemonControllerTest {

    AllPokemonController allPokemonController;

    @BeforeEach
    void setUp() {
        this.allPokemonController = new AllPokemonController();
        this.allPokemonController.userPokemonController =
                new UserPokemonController(new InMemoryRepository<>());
    }

    @Test
    void getAllPokemonEmbeds() {
        allPokemonController.getAllPokemonEmbeds("7777");
    }
}
