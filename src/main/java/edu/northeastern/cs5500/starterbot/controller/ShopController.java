package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.UserBalance;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Data
public class ShopController {

    GenericRepository<UserBalance> shopRepository;
    Integer userID;
    ArrayList<WildPokemon> previousPokemons;
    long previousVisitedTime;
    // use as an api
    @Inject WildPokemonController wildPokemonController;

    private static long TEN_MINUTES_IN_MILLISSECONDS = 600000l;

    @Inject
    public ShopController(GenericRepository<UserBalance> shopRepository) {
        this.shopRepository = shopRepository;
        this.previousPokemons = new ArrayList<>(3);
        // if this is the first time to use the shop feature, set it to default value of -1
        this.previousVisitedTime = -1;
    }

    @Nonnull
    public MessageBuilder getBalanceEB(String discordUserId, String preferredName) {

        EmbedBuilder eb = new EmbedBuilder();
        MessageBuilder mb = new MessageBuilder();

        SelectionMenu menu =
                SelectionMenu.create("shop")
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

    // for the show feature, create two selection menus
    @Nonnull
    public MessageBuilder getShowEB(
            String discordUserId, String preferredName, String discordChannelID) {

        EmbedBuilder eb = new EmbedBuilder();
        MessageBuilder mb = new MessageBuilder();
        long currentTime = System.currentTimeMillis();
        // if it is the first time to use the feature show
        // or the previousPokemons are empty
        // or the time diff from last visit is longer than 10 minutes
        // generate three random pokemons
        if (this.previousVisitedTime < 0
                || this.previousPokemons.size() < 3
                || currentTime - this.previousVisitedTime > TEN_MINUTES_IN_MILLISSECONDS) {
            // update the pokemons
            this.previousPokemons.clear();
            this.previousVisitedTime = currentTime;
            for (int i = 0; i < 3; i++) {
                wildPokemonController.updateWildPokemonForChannel(discordChannelID);
                this.previousPokemons.add(
                        wildPokemonController.getWildPokemonForChannel(discordChannelID));
            }
        }

        // firstly we need to find all the moves and create a random price for each of
        // the moves.

        // for moves, they only include the moves of the selected pokemon.

        // secondly, create a list of random pokemons for the user to redeem.
        // there will be a total of five random pokemons displayed.

        // if the message time is within a certain time, random pokemons will
        // NOT be updated.

        Integer currentBalance =
                getBalanceForChannel(discordUserId).getBalance() == null
                        ? 100
                        : getBalanceForChannel(discordUserId).getBalance();
        getBalanceForChannel(discordUserId).setBalance(currentBalance);
        shopRepository.update(getBalanceForChannel(discordUserId));

        StringBuilder movesToBuy = new StringBuilder();

        eb.setTitle("Welcome to the Shop!     " + preferredName)
                .setDescription("Your current balance: " + currentBalance);

        ArrayList<EmbedBuilder> ebArray = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ebArray.add(new EmbedBuilder());
        }

        for (int i = 0; i < 3; i++) {

            EmbedBuilder currentEmbedBuilder = ebArray.get(i);
            // for each pokemon, the price of it is a random number plus
            // 100 of balance.
            Random rand = new Random();
            int upperBound = 100;
            int price = rand.nextInt(upperBound) + upperBound;
            WildPokemon currentOne = this.previousPokemons.get(i);
            // for the specific attributes of the pokemon, we are not
            // disclosing to the user unless they buy it.
            StringBuilder pokemonsToBuy = new StringBuilder();
            currentEmbedBuilder.setTitle(
                    "\n" + currentOne.getPokemonInfo().getName() + "    Price: " + price);
            pokemonsToBuy.append("HP: " + currentOne.getPokemonInfo().getHp());
            pokemonsToBuy.append("\n" + "Attack: " + currentOne.getPokemonInfo().getAttack());
            pokemonsToBuy.append("\n" + "Defense: " + currentOne.getPokemonInfo().getDefense());
            currentEmbedBuilder.setDescription(pokemonsToBuy.toString());
            currentEmbedBuilder.setThumbnail(currentOne.getPokemonInfo().getOfficialArtworkUrl());
        }

        mb.setEmbeds(
                        eb.build(),
                        ebArray.get(0).build(),
                        ebArray.get(1).build(),
                        ebArray.get(2).build())
                .setActionRows(
                        ActionRow.of(
                                Button.primary("show:redeem", "redeem"),
                                Button.primary("show:buy", "buy")));

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
        UserBalance targetBalance = getBalanceForChannel(discordUserId);
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
