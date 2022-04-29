package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopControllerTest {

    private ShopController shopController;

    @BeforeEach
    void setUp() {
        this.shopController = new ShopController(new InMemoryRepository<>());
    }

    @Test
    void testGetBalanceForUserId() {

        // create a unique id
        String uniqueId = "12sdlfhlsdngslfndslnbglsl";
        assertNotNull(this.shopController.getBalanceForUserId(uniqueId));
        assertNotNull(this.shopController.getBalanceForUserId(uniqueId));
    }

    @Test
    void testGetPreviousPokemons() {}

    @Test
    void testGetPreviousVisitedTime() {}

    @Test
    void testGetPrices() {}

    @Test
    void testGetShopRepository() {}

    @Test
    void testGetUserID() {}

    @Test
    void testGetWildPokemonController() {}

    @Test
    void testSetPreviousPokemons() {}

    @Test
    void testSetPreviousVisitedTime() {}

    @Test
    void testSetPrices() {}

    @Test
    void testSetShopRepository() {}

    @Test
    void testSetUserID() {}

    @Test
    void testSetWildPokemonController() {}

    @Test
    void testUpdateBalanceForUserId() {}
}
