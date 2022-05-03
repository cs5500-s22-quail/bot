package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.*;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

@Singleton
@Slf4j
public class RedeemCommand implements Command, SelectionMenuHandler {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;
    @Inject UserPokemonController userPokemonController;
    @Inject DisplayController displayController;
    @Inject WildPokemonController wildPokemonController;
    private static long TEN_MINUTES_IN_MILLISSECONDS = 600000l;

    @Inject
    public RedeemCommand() {}

    @Override
    public String getName() {
        return "redeem";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "Please choose a pokemon from the dropdown menu to make a purchase.");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /redeem");
        String discordUserId = event.getUser().getId();
        String preferredName = userPreferenceController.getPreferredNameForUser(discordUserId);
        String discordChannelId = event.getChannel().getId();
        event.reply(getRedeemEB(discordUserId, preferredName, discordChannelId).build()).queue();
    }

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        // event.reply(event.getInteraction().getValues().get(0)).queue();
        String discordUserId = event.getUser().getId();
        // add the chosen pokemon into the catched ones.
        String pokemonNameChosen = event.getInteraction().getValues().get(0);
        for (WildPokemon wildPokemon : shopController.getPreviousPokemons()) {
            if (wildPokemon.getPokemonInfo().getName().equals(pokemonNameChosen)) {
                userPokemonController.addPokemon(wildPokemon.getPokemonInfo(), discordUserId);
                int index = shopController.getPreviousPokemons().indexOf(wildPokemon);
                int price = shopController.getPrices().get(index);
                // validate whether the price is affordable or not.
                if (shopController.getBalanceForUserId(discordUserId).getBalance() < price) {
                    event.reply(
                                    "Sorry. You do not have enough coins for "
                                            + event.getInteraction().getValues().get(0)
                                            + ".")
                            .queue();
                    ;
                    return;
                }
                // update the balance if the purchase is processed.
                shopController.updateBalanceForUserId(discordUserId, -price);

                MessageBuilder mb = new MessageBuilder();
                EmbedBuilder embedBuilder =
                        new EmbedBuilder()
                                .setTitle(
                                        "You have bought "
                                                + wildPokemon.getPokemonInfo().getName()
                                                + " at "
                                                + price
                                                + " coins!\n")
                                .setImage(wildPokemon.getPokemonInfo().getOfficialArtworkUrl());

                Quality quality = wildPokemon.getPokemonInfo().getIv().getQuality();
                String pokemonQuality = "";
                switch (quality) {
                    case RED:
                        embedBuilder = embedBuilder.setColor(0xff210d);
                        pokemonQuality = "Legendary";
                        break;
                    case PURPLE:
                        embedBuilder = embedBuilder.setColor(0xc30dff);
                        pokemonQuality = "Epic";
                        break;
                    case BLUE:
                        embedBuilder = embedBuilder.setColor(0x0d7eff);
                        pokemonQuality = "Superior";
                        break;
                    case GREEN:
                        embedBuilder = embedBuilder.setColor(0x0dff82);
                        pokemonQuality = "Good";
                        break;
                    default:
                        break;
                }
                embedBuilder.setDescription(
                        "Quality: "
                                + pokemonQuality
                                + System.lineSeparator()
                                + displayController.PokemonInfoUI(wildPokemon.getPokemonInfo())
                                + System.lineSeparator());
                mb.setEmbeds(embedBuilder.build());

                event.reply(mb.build()).queue();
                ;
                return;
            }
        }

        event.reply(
                        "Purchase of "
                                + event.getInteraction().getValues().get(0)
                                + " went wrong, try again.")
                .queue();
    }

    @Nonnull
    private MessageBuilder getRedeemEB(
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
        sb1.append("Here are the pokemons on sale!");
        sb1.append(System.lineSeparator());
        sb1.append("Please chooose one from the dropdown menu!");

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
            pokemonsToBuy.append("\n\nHP: " + currentOne.getPokemonInfo().getHp());
            pokemonsToBuy.append("\n" + "Attack: " + currentOne.getPokemonInfo().getAttack());
            pokemonsToBuy.append("\n" + "Defense: " + currentOne.getPokemonInfo().getDefense());
            long percentage =
                    Math.round(currentOne.getPokemonInfo().getIv().getIVPercentage() * 10000);
            long partA = percentage / 100;
            long partB = percentage - partA * 100;
            pokemonsToBuy.append("\n" + "Total IV " + partA + "." + partB + "%");
            currentEmbedBuilder.setDescription(pokemonsToBuy.toString());
            currentEmbedBuilder.setThumbnail(currentOne.getPokemonInfo().getOfficialArtworkUrl());
        }

        SelectionMenu menu =
                SelectionMenu.create("redeem")
                        .setPlaceholder("Please choose your pokemon to buy.")
                        .addOption(
                                shopController.previousPokemons.get(0).getPokemonInfo().getName(),
                                shopController
                                        .previousPokemons
                                        .get(0)
                                        .getPokemonInfo()
                                        .getName()) // the first pokemon
                        .addOption(
                                shopController.previousPokemons.get(1).getPokemonInfo().getName(),
                                shopController
                                        .previousPokemons
                                        .get(1)
                                        .getPokemonInfo()
                                        .getName()) // the second
                        .addOption(
                                shopController.previousPokemons.get(2).getPokemonInfo().getName(),
                                shopController.previousPokemons.get(2).getPokemonInfo().getName())
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
}
