package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
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
        assertThat(this.userPokemonController.getUserPokemonForMemberId("7777")).isNotNull();
        assertThat(this.userPokemonController.getUserPokemonForMemberId("7777").getPokemonTeam())
                .isNotNull();
        assertThat(this.userPokemonController.getUserPokemonForMemberId("Random")).isNotNull();
        assertThat(this.userPokemonController.getUserPokemonForMemberId("Random").getPokemonTeam())
                .isNotNull();
    }

    @Test
    void addPokemon() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("888");
        assertThat(this.userPokemonController.hasCarriedPokemon("888")).isFalse();

        userPokemonController.addPokemon(pokemonService.fromID(4), "888");
        assertThat(this.userPokemonController.hasCarriedPokemon("888")).isTrue();
        userPokemonController.addPokemon(pokemonService.fromID(5), "888");
        userPokemonController.addPokemon(pokemonService.fromID(6), "888");

        assertThat(userPokemonController.getUserPokemonForMemberId("888").getPokemonTeam().size())
                .isEqualTo(3);
    }

    @Test
    void isPossess() {
        assertThat(this.userPokemonController.isPossess("random", "7777")).isFalse();
        assertThat(this.userPokemonController.isPossess("random", "random")).isFalse();
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
        assertThat(userPokemonController.getUserPokemonForMemberId("999").getCarriedPokemon())
                .isEqualTo(
                        userPokemonController
                                .getUserPokemonForMemberId("999")
                                .getPokemonTeam()
                                .get(2 - 1));
    }

    @Test
    void probability() {
        assertThat(userPokemonController.probability(Quality.RED)).isEqualTo(2);
        assertThat(userPokemonController.probability(Quality.PURPLE)).isEqualTo(3);
        assertThat(userPokemonController.probability(Quality.BLUE)).isEqualTo(5);
        assertThat(userPokemonController.probability(Quality.GREEN)).isEqualTo(7);
    }

    @Test
    void testAttemptCatch() {
        assertThat(userPokemonController.AttemptCatch(Quality.GREEN)).isNotNull();
        assertThat(userPokemonController.AttemptCatch(Quality.RED)).isNotNull();
    }

    @Test
    void updateUserPokemon() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("1111");
        userPokemon.setCarriedPokemon(pokemonService.fromID(124));
        this.userPokemonController.updateUserPokemon(userPokemon);

        assertThat(userPokemon.getCarriedPokemon()).isNotNull();
    }

    @Test
    void levelUp() {
        UserPokemon userPokemon = this.userPokemonController.getUserPokemonForMemberId("1111");
        userPokemon.setCarriedPokemon(pokemonService.fromID(124));
        this.userPokemonController.levelUp(userPokemon);
    }
}
