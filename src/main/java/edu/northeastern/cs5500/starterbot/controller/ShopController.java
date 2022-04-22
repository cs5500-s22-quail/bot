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
    ArrayList<Integer> prices;
    // use as an api
    @Inject WildPokemonController wildPokemonController;

    private static long TEN_MINUTES_IN_MILLISSECONDS = 600000l;

    @Inject
    public ShopController(GenericRepository<UserBalance> shopRepository) {
        this.shopRepository = shopRepository;
        this.previousPokemons = new ArrayList<>(3);
        // if this is the first time to use the shop feature, set it to default value of -1
        this.previousVisitedTime = -1;
        this.prices = new ArrayList<>(3);
    }

    @Nonnull
    public MessageBuilder getBalanceEB(String discordUserId, String preferredName) {

        EmbedBuilder eb = new EmbedBuilder();
        MessageBuilder mb = new MessageBuilder();

        Integer currentBalance =
                getBalanceForUserId(discordUserId).getBalance() == null
                        ? 100
                        : getBalanceForUserId(discordUserId).getBalance();
        getBalanceForUserId(discordUserId).setBalance(currentBalance);
        shopRepository.update(getBalanceForUserId(discordUserId));
        eb.setTitle("Welcome to the Shop!     " + preferredName)
                .setDescription(
                        "Your current balance: "
                                + currentBalance
                                + "\n\n To view available pokemons, please Click the show button");

        eb.setImage(
                "https://cdn.images.express.co.uk/img/dynamic/143/590x/Pokemon-Center-London-1192560.jpg?r=1571521317165");
        mb.setEmbeds(eb.build()).setActionRows(ActionRow.of(Button.primary("shop:show", "show")));

        return mb;

        // set the component for the EB
    }

    // for the show feature, create two buttons
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
            this.prices.clear();
            this.previousVisitedTime = currentTime;
            for (int i = 0; i < 3; i++) {
                wildPokemonController.updateWildPokemonForChannel(discordChannelID);
                this.previousPokemons.add(
                        wildPokemonController.getWildPokemonForChannel(discordChannelID));
                Random rand = new Random();
                int upperBound = 100;
                int price = rand.nextInt(upperBound) + upperBound;
                this.prices.add(price);
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
                getBalanceForUserId(discordUserId).getBalance() == null
                        ? 100
                        : getBalanceForUserId(discordUserId).getBalance();
        getBalanceForUserId(discordUserId).setBalance(currentBalance);
        shopRepository.update(getBalanceForUserId(discordUserId));

        eb.setTitle("Welcome to the Shop!     " + preferredName)
                .setDescription(
                        "Your current balance: "
                                + currentBalance
                                + "\n\nHere are some example pokemons!"
                                + "\nUse /redeem Command to see what pokemons are on sale!");

        ArrayList<EmbedBuilder> ebArray = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ebArray.add(new EmbedBuilder());
        }

        for (int i = 0; i < 3; i++) {

            EmbedBuilder currentEmbedBuilder = ebArray.get(i);
            // for each pokemon, the price of it is a random number plus
            // 100 of balance.
            WildPokemon currentOne = this.previousPokemons.get(i);
            // for the specific attributes of the pokemon, we are not
            // disclosing to the user unless they buy it.
            StringBuilder pokemonsToBuy = new StringBuilder();
            Quality quality = currentOne.getPokemonInfo().getIv().getQuality();
            String pokemonQuality = "";
            switch (quality) {
                case RED:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0xff210d);
                    pokemonQuality = "Legendary";
                    break;
                case PURPLE:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0xc30dff);
                    pokemonQuality = "Epic";
                    break;
                case BLUE:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0x0d7eff);
                    pokemonQuality = "Superior";
                    break;
                case GREEN:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0x0dff82);
                    pokemonQuality = "Good";
                    break;
                default:
                    break;
            }
            currentEmbedBuilder.setTitle(
                    "\n"
                            + currentOne.getPokemonInfo().getName()
                            + "    Price: "
                            + this.prices.get(i));
            pokemonsToBuy.append("Quality: " + pokemonQuality);
            pokemonsToBuy.append("\n\nHP: " + currentOne.getPokemonInfo().getHp());
            pokemonsToBuy.append("\n" + "Attack: " + currentOne.getPokemonInfo().getAttack());
            pokemonsToBuy.append("\n" + "Defense: " + currentOne.getPokemonInfo().getDefense());
            pokemonsToBuy.append(
                    "\n"
                            + "Total IV "
                            + currentOne.getPokemonInfo().getIv().getIVPercentage() * 100
                            + "%");
            currentEmbedBuilder.setDescription(pokemonsToBuy.toString());
            currentEmbedBuilder.setThumbnail(currentOne.getPokemonInfo().getOfficialArtworkUrl());
        }

        mb.setEmbeds(
                eb.build(), ebArray.get(0).build(), ebArray.get(1).build(), ebArray.get(2).build());

        return mb;

        // set the component for the EB
    }

    @Nonnull
    public MessageBuilder getRedeemEB(
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
            this.prices.clear();
            this.previousVisitedTime = currentTime;
            for (int i = 0; i < 3; i++) {
                wildPokemonController.updateWildPokemonForChannel(discordChannelID);
                this.previousPokemons.add(
                        wildPokemonController.getWildPokemonForChannel(discordChannelID));
                Random rand = new Random();
                int upperBound = 100;
                int price = rand.nextInt(upperBound) + upperBound;
                this.prices.add(price);
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
                getBalanceForUserId(discordUserId).getBalance() == null
                        ? 100
                        : getBalanceForUserId(discordUserId).getBalance();
        getBalanceForUserId(discordUserId).setBalance(currentBalance);
        shopRepository.update(getBalanceForUserId(discordUserId));

        eb.setTitle("Welcome to the Shop!     " + preferredName)
                .setDescription(
                        "Your current balance: "
                                + currentBalance
                                + "\n\nHere are the pokemons on sale!"
                                + "\nPlease chooose one from the dropdown menu!");

        ArrayList<EmbedBuilder> ebArray = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ebArray.add(new EmbedBuilder());
        }

        for (int i = 0; i < 3; i++) {

            EmbedBuilder currentEmbedBuilder = ebArray.get(i);
            // for each pokemon, the price of it is a random number plus
            // 100 of balance.
            WildPokemon currentOne = this.previousPokemons.get(i);
            // for the specific attributes of the pokemon, we are not
            // disclosing to the user unless they buy it.
            StringBuilder pokemonsToBuy = new StringBuilder();
            Quality quality = currentOne.getPokemonInfo().getIv().getQuality();
            String pokemonQuality = "";
            switch (quality) {
                case RED:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0xff210d);
                    pokemonQuality = "Legendary";
                    break;
                case PURPLE:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0xc30dff);
                    pokemonQuality = "Epic";
                    break;
                case BLUE:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0x0d7eff);
                    pokemonQuality = "Superior";
                    break;
                case GREEN:
                    currentEmbedBuilder = currentEmbedBuilder.setColor(0x0dff82);
                    pokemonQuality = "Good";
                    break;
                default:
                    break;
            }
            currentEmbedBuilder.setTitle(
                    "\n"
                            + currentOne.getPokemonInfo().getName()
                            + "    Price: "
                            + this.prices.get(i));
            pokemonsToBuy.append("Quality: " + pokemonQuality);
            pokemonsToBuy.append("\n\nHP: " + currentOne.getPokemonInfo().getHp());
            pokemonsToBuy.append("\n" + "Attack: " + currentOne.getPokemonInfo().getAttack());
            pokemonsToBuy.append("\n" + "Defense: " + currentOne.getPokemonInfo().getDefense());
            long percentage =
                    Math.round(currentOne.getPokemonInfo().getIv().getIVPercentage() * 10000);
            long partA = percentage / 100;
            long partB = percentage - partA * 100;
            pokemonsToBuy.append("\n" + "Total IV " + partA + partB + "%");
            currentEmbedBuilder.setDescription(pokemonsToBuy.toString());
            currentEmbedBuilder.setThumbnail(currentOne.getPokemonInfo().getOfficialArtworkUrl());
        }

        SelectionMenu menu =
                SelectionMenu.create("redeem")
                        .setPlaceholder("Please choose your pokemon to buy.")
                        .addOption(
                                this.previousPokemons.get(0).getPokemonInfo().getName(),
                                this.previousPokemons
                                        .get(0)
                                        .getPokemonInfo()
                                        .getName()) // the first pokemon
                        .addOption(
                                this.previousPokemons.get(1).getPokemonInfo().getName(),
                                this.previousPokemons
                                        .get(1)
                                        .getPokemonInfo()
                                        .getName()) // the second
                        .addOption(
                                this.previousPokemons.get(2).getPokemonInfo().getName(),
                                this.previousPokemons.get(2).getPokemonInfo().getName())
                        .build();

        mb.setEmbeds(
                        eb.build(),
                        ebArray.get(0).build(),
                        ebArray.get(1).build(),
                        ebArray.get(2).build())
                .setActionRows(ActionRow.of(menu));

        return mb;

        // set the component for the EB
    }

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
