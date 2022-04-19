package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPokemonControllerTest {

    UserPokemonController userPokemonController;

    @BeforeEach
    void setUp() {
        this.userPokemonController = new UserPokemonController(new InMemoryRepository<>());
    }

    @Test
    void getUserPokemonForMemberID() {
        assertNotNull(this.userPokemonController.getUserPokemonForMemberID("7777"));
        assertNotNull(
                this.userPokemonController.getUserPokemonForMemberID("7777").getPokemonTeam());
        assertNotNull(this.userPokemonController.getUserPokemonForMemberID("Random"));
        assertNotNull(
                this.userPokemonController.getUserPokemonForMemberID("Random").getPokemonTeam());
    }

    @Test
    void addPokemon() {}

    @Test
    void isPossess() {
        assertFalse(this.userPokemonController.isPossess("random", "7777"));
        assertFalse(this.userPokemonController.isPossess("random", "random"));
    }

    @Test
    void attemptCatch() {}

    @Test
    void probability() {}
}
