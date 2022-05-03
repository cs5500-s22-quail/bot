package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.Quality;
import edu.northeastern.cs5500.starterbot.controller.ShopController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class ShowCommand implements Command {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;
    @Inject WildPokemonController wildPokemonController;
    private static long TEN_MINUTES_IN_MILLISSECONDS = 600000l;

    @Inject
    public ShowCommand() {}

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "You could redeem your balance for the following moves and pokemons");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /show");

        String discordUserId = event.getUser().getId();
        String preferredName = userPreferenceController.getPreferredNameForUser(discordUserId);
        String discordChannelId = event.getChannel().getId();

        event.reply(getShowEB(discordUserId, preferredName, discordChannelId).build()).queue();
    }

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
        if (shopController.previousVisitedTime < 0
                || shopController.previousPokemons.size() < 3
                || currentTime - shopController.previousVisitedTime
                        > TEN_MINUTES_IN_MILLISSECONDS) {
            // update the pokemons
            shopController.previousPokemons.clear();
            shopController.prices.clear();
            shopController.previousVisitedTime = currentTime;
            for (int i = 0; i < 3; i++) {
                wildPokemonController.updateWildPokemonForChannel(discordChannelID);
                shopController.previousPokemons.add(
                        wildPokemonController.getWildPokemonForChannel(discordChannelID));
                Random rand = new Random();
                int upperBound = 100;
                int price = rand.nextInt(upperBound) + upperBound;
                shopController.prices.add(price);
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
                shopController.getBalanceForUserId(discordUserId).getBalance() == null
                        ? 100
                        : shopController.getBalanceForUserId(discordUserId).getBalance();
        shopController.getBalanceForUserId(discordUserId).setBalance(currentBalance);
        shopController.shopRepository.update(shopController.getBalanceForUserId(discordUserId));

        StringBuilder sb1 = new StringBuilder();
        sb1.append("Your current balance: ");
        sb1.append(currentBalance);
        sb1.append(System.lineSeparator());
        sb1.append(System.lineSeparator());
        sb1.append("Here are some example pokemons!");
        sb1.append(System.lineSeparator());
        sb1.append("Use /redeem Command to see what pokemons are on sale!");

        eb.setTitle("Welcome to the Shop!     " + preferredName).setDescription(sb1.toString());

        ArrayList<EmbedBuilder> ebArray = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            ebArray.add(new EmbedBuilder());
        }

        for (int i = 0; i < 3; i++) {

            EmbedBuilder currentEmbedBuilder = ebArray.get(i);
            // for each pokemon, the price of it is a random number plus
            // 100 of balance.
            WildPokemon currentOne = shopController.previousPokemons.get(i);
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
                            + shopController.prices.get(i));
            pokemonsToBuy.append("Quality: " + pokemonQuality);
            pokemonsToBuy.append(System.lineSeparator());
            pokemonsToBuy.append(System.lineSeparator());
            pokemonsToBuy.append("HP: " + currentOne.getPokemonInfo().getHp());
            pokemonsToBuy.append(System.lineSeparator());
            pokemonsToBuy.append("Attack: ");
            pokemonsToBuy.append(currentOne.getPokemonInfo().getAttack());
            pokemonsToBuy.append(System.lineSeparator());
            pokemonsToBuy.append("Defense: ");
            pokemonsToBuy.append(currentOne.getPokemonInfo().getDefense());
            long percentage =
                    Math.round(currentOne.getPokemonInfo().getIv().getIVPercentage() * 10000);
            long partA = percentage / 100;
            long partB = percentage - partA * 100;
            pokemonsToBuy.append(System.lineSeparator());
            pokemonsToBuy.append("Total IV ");
            pokemonsToBuy.append(partA);
            pokemonsToBuy.append(".");
            pokemonsToBuy.append(partB);
            pokemonsToBuy.append("%");
            currentEmbedBuilder.setDescription(pokemonsToBuy.toString());
            currentEmbedBuilder.setThumbnail(currentOne.getPokemonInfo().getOfficialArtworkUrl());
        }

        mb.setEmbeds(
                eb.build(), ebArray.get(0).build(), ebArray.get(1).build(), ebArray.get(2).build());

        return mb;

        // set the component for the EB
    }
}
