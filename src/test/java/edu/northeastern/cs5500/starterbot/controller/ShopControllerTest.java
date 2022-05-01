package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
