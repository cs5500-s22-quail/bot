package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.Quality;
import edu.northeastern.cs5500.starterbot.controller.ShopController;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class RedeemCommand implements Command, SelectionMenuHandler {

    @Inject UserPreferenceController userPreferenceController;
    @Inject ShopController shopController;
    @Inject UserPokemonController userPokemonController;

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
        event.reply(
                        shopController
                                .getRedeemEB(discordUserId, preferredName, discordChannelId)
                                .build())
                .queue();
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
                                        "You have bought"
                                                + wildPokemon.getPokemonInfo().getName()
                                                + " at "
                                                + price
                                                + "coins!\n")
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
                                + ivUIBundle(wildPokemon.getPokemonInfo())
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

    private String ivUIBundle(PokemonInfo pokemonInfo) {
        return ivUI("HP", pokemonInfo.getHp(), pokemonInfo.getIv().getHp())
                + ivUI("Attack", pokemonInfo.getAttack(), pokemonInfo.getIv().getAttack())
                + ivUI("Defense", pokemonInfo.getDefense(), pokemonInfo.getIv().getDefense())
                + ivUI(
                        "Sp.Atk",
                        pokemonInfo.getSpecialAttack(),
                        pokemonInfo.getIv().getSpecialAttack())
                + ivUI(
                        "Sp.Def",
                        pokemonInfo.getSpecialDefense(),
                        pokemonInfo.getIv().getSpecialDefense())
                + ivUI("Speed", pokemonInfo.getSpeed(), pokemonInfo.getIv().getSpeed())
                + System.lineSeparator()
                + "Total IV: "
                + pokemonInfo.getIv().getIVPercentageFormat();
    }

    private String ivUI(String statName, int baseStat, int iv) {
        return System.lineSeparator() + statName + ": " + baseStat + " - IV: " + iv + "/31";
    }
}
