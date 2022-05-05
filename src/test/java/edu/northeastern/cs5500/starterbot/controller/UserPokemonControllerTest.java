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
    void getUserPokemonForMemberId() {
        assertNotNull(this.userPokemonController.getUserPokemonForMemberId("7777"));
        assertNotNull(
                this.userPokemonController.getUserPokemonForMemberId("7777").getPokemonTeam());
        assertNotNull(this.userPokemonController.getUserPokemonForMemberId("Random"));
        assertNotNull(
                this.userPokemonController.getUserPokemonForMemberId("Random").getPokemonTeam());
    }

    @Test
    void addPokemon() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("888");
        assertFalse(this.userPokemonController.hasCarriedPokemon("888"));
        userPokemonController.addPokemon(pokemonService.fromID(4), "888");
        assertTrue(this.userPokemonController.hasCarriedPokemon("888"));
        userPokemonController.addPokemon(pokemonService.fromID(5), "888");
        userPokemonController.addPokemon(pokemonService.fromID(6), "888");
        assertEquals(
                3, userPokemonController.getUserPokemonForMemberId("888").getPokemonTeam().size());
    }

    @Test
    void isPossess() {
        assertFalse(this.userPokemonController.isPossess("random", "7777"));
        assertFalse(this.userPokemonController.isPossess("random", "random"));
    }

    @Test
    void updateCarriedPokemonForMemberID() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("999");
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> userPokemonController.updateCarriedPokemonForMemberId("999", 1));
        userPokemonController.addPokemon(pokemonService.fromID(4), "999");
        userPokemonController.addPokemon(pokemonService.fromID(5), "999");
        userPokemonController.updateCarriedPokemonForMemberId("999", 2);
        assertEquals(
                userPokemonController.getUserPokemonForMemberId("999").getCarriedPokemon(),
                userPokemonController.getUserPokemonForMemberId("999").getPokemonTeam().get(2 - 1));
    }

    @Test
    void probability() {
        assertEquals(userPokemonController.probability(Quality.RED), 2);
        assertEquals(userPokemonController.probability(Quality.PURPLE), 3);
        assertEquals(userPokemonController.probability(Quality.BLUE), 5);
        assertEquals(userPokemonController.probability(Quality.GREEN), 7);
    }

    @Test
    void testAttemptCatch() {
        assertNotNull(userPokemonController.AttemptCatch(Quality.GREEN));
        assertNotNull(userPokemonController.AttemptCatch(Quality.GREEN));
        assertNotNull(userPokemonController.AttemptCatch(Quality.RED));
    }

    @Test
    void updateUserPokemon() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("1111");
        userPokemon.setCarriedPokemon(pokemonService.fromID(124));

        this.userPokemonController.updateUserPokemon(userPokemon);
        assertNotNull(userPokemon.getCarriedPokemon());
    }

    @Test
    void levelUp() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("1111");
        userPokemon.setCarriedPokemon(pokemonService.fromID(124));
        this.userPokemonController.levelUp(userPokemon);
    }
}
