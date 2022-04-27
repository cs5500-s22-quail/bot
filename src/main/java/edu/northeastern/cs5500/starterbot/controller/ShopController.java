package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.UserBalance;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.Data;

@Data
public class ShopController {

    public GenericRepository<UserBalance> shopRepository;
    public Integer userID;
    public ArrayList<WildPokemon> previousPokemons;
    public long previousVisitedTime;
    public ArrayList<Integer> prices;
    // use as an api
    @Inject WildPokemonController wildPokemonController;

    @Inject
    public ShopController(GenericRepository<UserBalance> shopRepository) {
        this.shopRepository = shopRepository;
        this.previousPokemons = new ArrayList<>(3);
        // if this is the first time to use the shop feature, set it to default value of -1
        this.previousVisitedTime = -1;
        this.prices = new ArrayList<>(3);
    }

    // for the show feature, create two buttons

    // get the userbalance according to discordId
    @Nonnull
    public UserBalance getBalanceForUserId(String discordUserId) {
        Collection<UserBalance> userBalances = shopRepository.getAll();
        for (UserBalance userBalance : userBalances) {
            if (userBalance.getDiscordUserId().equals(discordUserId)) return userBalance;
        }

        // if this user balance has not been created before, just create a new one
        UserBalance userBalance = new UserBalance();
        userBalance.setDiscordUserId(discordUserId);
        userBalance.setBalance(Integer.valueOf(100));
        shopRepository.add(userBalance);
        return userBalance;
    }

    @Nonnull
    public UserBalance updateBalanceForUserId(String discordUserId, Integer valueToAdd) {
        Collection<UserBalance> userBalances = shopRepository.getAll();
        UserBalance targetBalance = getBalanceForUserId(discordUserId);
        for (UserBalance userBalance : userBalances) {
            if (userBalance.getDiscordUserId().equals(discordUserId)) {
                targetBalance = userBalance;
            }
        }
        Integer currentBalance =
                targetBalance.getBalance() == null ? 100 : targetBalance.getBalance();
        targetBalance.setBalance(currentBalance);

        if (Integer.MAX_VALUE - currentBalance < valueToAdd) {
            throw new IllegalArgumentException(
                    "The value cannot be added for exceeding Integer range!");
        }

        currentBalance += valueToAdd;

        if (currentBalance < 0) {
            currentBalance -= valueToAdd;
            throw new IllegalArgumentException("You balance could not be less than 0");
        }
        targetBalance.setBalance(currentBalance);
        shopRepository.update(targetBalance);
        return targetBalance;
    }
}
