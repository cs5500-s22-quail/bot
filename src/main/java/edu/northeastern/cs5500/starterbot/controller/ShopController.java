package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.UserBalance;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Data
public class ShopController {

    GenericRepository<UserBalance> shopRepository;
    Integer userID;

    @Inject
    public ShopController(GenericRepository<UserBalance> shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Nonnull
    public MessageBuilder getBalanceEB(String discordUserId, String preferredName) {

        EmbedBuilder eb = new EmbedBuilder();
        MessageBuilder mb = new MessageBuilder();

        SelectionMenu menu =
                SelectionMenu.create("dropdown")
                        .setPlaceholder("Please choose your further step.")
                        .addOption("Buy", "buy") // make a purchase from the list of items
                        .addOption("Show", "show") // display all the items to buy
                        .addOption(
                                "Redeem", "redeem") // redeem a random list of pokemons with balance
                        .build();

        Integer currentBalance =
                getBalanceForChannel(discordUserId).getBalance() == null
                        ? 100
                        : getBalanceForChannel(discordUserId).getBalance();
        getBalanceForChannel(discordUserId).setBalance(currentBalance);
        shopRepository.update(getBalanceForChannel(discordUserId));
        eb.setTitle("Welcome to the Shop!     " + preferredName)
                .setDescription("Your current balance: " + currentBalance);
        mb.setEmbeds(eb.build()).setActionRows(ActionRow.of(menu));

        return mb;

        // set the component for the EB
    }

    // get the userbalance according to discordId
    @Nonnull
    public UserBalance getBalanceForChannel(String discordUserId) {
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
    public UserBalance addBalanceForChannel(String discordUserId, Integer valueToAdd) {
        Collection<UserBalance> userBalances = shopRepository.getAll();
        UserBalance targetBalance = new UserBalance();
        for (UserBalance userBalance : userBalances) {
            if (userBalance.getDiscordUserId().equals(discordUserId)) {
                targetBalance = userBalance;
            }
        }
        Integer currentBalance =
                targetBalance.getBalance() == null ? 100 : targetBalance.getBalance();
        targetBalance.setBalance(currentBalance);

        if (Integer.MAX_VALUE - valueToAdd < currentBalance) {
            throw new IllegalArgumentException(
                    "The value cannot be added for exceeding Integer range!");
        }
        currentBalance += valueToAdd;
        targetBalance.setBalance(currentBalance);
        shopRepository.update(targetBalance);
        return targetBalance;
    }
}
