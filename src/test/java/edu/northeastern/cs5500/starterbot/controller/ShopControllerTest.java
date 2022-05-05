package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

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

        assertThat(this.shopController.getBalanceForUserId(uniqueId)).isNotNull();
        assertThat(this.shopController.getBalanceForUserId(uniqueId)).isNotNull();
    }

    @Test
    void testUpdateBalanceForUserId() {
        assertThat(shopController.updateBalanceForUserId(uniqueId, 0));
    }

    @Test
    void testUpdateBalanceForUserIdFail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> shopController.updateBalanceForUserId(uniqueId, -1100));
        assertThrows(
                IllegalArgumentException.class,
                () -> shopController.updateBalanceForUserId(uniqueId, Integer.MAX_VALUE));
    }

    @Test
    void testEquals() {
        ShopController shopController2 = new ShopController(new InMemoryRepository<>());
        assertThat(shopController2).isNotEqualTo(this.shopController);
        ShopController shopController3 = new ShopController(new InMemoryRepository<>());
        assertThat(shopController2).isNotEqualTo(shopController3);
        // since the inmemory repository does not have an overriden equals, hence equals does not
        // work.

    }

    @Test
    void testHashCode() {
        ShopController shopController2 = new ShopController(new InMemoryRepository<>());
        assertThat(shopController2.hashCode()).isNotEqualTo(this.shopController.hashCode());
    }

    @Test
    void testGettersForFields() {
        assertThat(this.shopController.getPreviousPokemons()).isNotNull();

        assertThat(this.shopController.getPreviousVisitedTime()).isNotNull();

        assertThat(this.shopController.getShopRepository()).isNotNull();

        assertThat(this.shopController.getUserID())
                .isNull(); // the UserId has not been instantiated, hence is null
    }
}
