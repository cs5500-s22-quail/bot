package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopControllerTest {

    private ShopController shopController;
    private static String uniqueId = "12sdlfhlsdngslfndslnbglsl";

    @BeforeEach
    void setUp() {
        this.shopController = new ShopController(new InMemoryRepository<>());
    }

    @Test
    void testGetBalanceForUserId() {

        // create a unique id
        
        assertNotNull(this.shopController.getBalanceForUserId(uniqueId));
        assertNotNull(this.shopController.getBalanceForUserId(uniqueId));
    }


    @Test
    void testUpdateBalanceForUserId() {
        shopController.updateBalanceForUserId(uniqueId, 0);
    }

    @Test
    void testUpdateBalanceForUserIdFail() {
        assertThrows(IllegalArgumentException.class, () -> shopController.updateBalanceForUserId(uniqueId, -1100));
        assertThrows(IllegalArgumentException.class, () -> shopController.updateBalanceForUserId(uniqueId, Integer.MAX_VALUE));
    }

    @Test
    void testEquals() {
        ShopController shopController2 = new ShopController(new InMemoryRepository<>());
        assertNotEquals(shopController2, this.shopController);
        ShopController shopController3 = new ShopController(new InMemoryRepository<>());
        assertNotEquals(shopController2, shopController3);
        // since the inmemory repository does not have an overriden equals, hence equals does not work.
        
    }

    @Test
    void testHashCode() {
        ShopController shopController2 = new ShopController(new InMemoryRepository<>());
        assertNotEquals(shopController2.hashCode(), this.shopController.hashCode());

    }

    @Test
    void testGettersForFields() {
        assertNotNull(this.shopController.getPreviousPokemons());
        assertNotNull(this.shopController.getPreviousVisitedTime());
        assertNotNull(this.shopController.getShopRepository());
        assertNull(this.shopController.getUserID()); // the UserId has not been instantiated, hence is null
    }
}
