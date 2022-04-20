package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPokemonControllerTest {

    UserPokemonController userPokemonController;
    PokemonService pokemonService = new PokemonService();
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
    void addPokemon() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberID("888");
        assertFalse(this.userPokemonController.hasCarriedPokemon("888"));
        userPokemonController.addPokemon(pokemonService.fromID(4), "888");
        assertTrue(this.userPokemonController.hasCarriedPokemon("888"));

    }

    @Test
    void isPossess() {
        assertFalse(this.userPokemonController.isPossess("random", "7777"));
        assertFalse(this.userPokemonController.isPossess("random", "random"));
    }

    @Test
    void updateCarriedPokemonForMemberID() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberID("999");
        assertThrows(IndexOutOfBoundsException.class, ()->userPokemonController.updateCarriedPokemonForMemberID("999", 1));
        userPokemonController.addPokemon(pokemonService.fromID(4), "999");
        userPokemonController.addPokemon(pokemonService.fromID(5), "999");
        userPokemonController.updateCarriedPokemonForMemberID("999", 2);
        assertEquals(userPokemonController.getUserPokemonForMemberID("999").getCarriedPokemon(),
                userPokemonController.getUserPokemonForMemberID("999").getPokemonTeam().get(2 - 1));
    }

    @Test
    void attemptCatch() {}

    @Test
    void probability() {}
}
