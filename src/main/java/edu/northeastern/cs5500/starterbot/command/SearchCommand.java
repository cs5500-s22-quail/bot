package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.*;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class SearchCommand implements Command, ButtonClickHandler {
    @Inject WildPokemonController wildPokemonController;
    @Inject UserPokemonController userPokemonController;
    @Inject ShopController shopController;
    @Inject DisplayController displayController;
    private static final int COST_PER_CATCH = 3;

    @Inject
    public SearchCommand() {}

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public CommandData getCommandData() {
        // /search
        return new CommandData(getName(), "Search nearby wild pokemon");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /search");
        String channelID = event.getMessageChannel().getId();

        wildPokemonController.updateWildPokemonForChannel(channelID);

        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(channelID);

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("A " + wildPokemon.getPokemonInfo().getName() + " has appeared!")
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
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "Click Catch button to catch the pokemon. "
                        + System.lineSeparator()
                        + "This will cost you "
                        + COST_PER_CATCH
                        + " coins. (Your Current Balance: "
                        + shopController.getBalanceForUserId(event.getUser().getId()).getBalance()
                        + " coins)");

        MessageBuilder mb = new MessageBuilder();

        mb.setEmbeds(embedBuilder.build())
                .setActionRows(
                        ActionRow.of(
                                Button.primary("search:catch", "Catch"),
                                Button.secondary("search:letGo", "LetGo")));

        event.reply(mb.build()).queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {

        String id = event.getButton().getId();
        String buttonName = id.split(":", 2)[1];

        if (buttonName.equals("catch")) {
            clickCatch(event);
        } else if (buttonName.equals("letGO")) {
            clickLetGo(event);
        }
    }

    public void clickLetGo(ButtonClickEvent event) {
        String channelId = event.getMessageChannel().getId();
        wildPokemonController.deletePokemonInfoForChannel(channelId);
        event.reply("The Pokemon has ran away");
    }

    public void clickCatch(ButtonClickEvent event) {

        String channelId = event.getMessageChannel().getId();

        if (!wildPokemonController.hasWildPokemonForChannel(channelId)) {
            log.info("catching status: do not have wild pokemon in this channel");
            EmbedBuilder embedBuilder =
                    new EmbedBuilder()
                            .setTitle(
                                    "use /search command to find pokemon in this channel before catching");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        String userId = event.getUser().getId();

        WildPokemon wildPokemon = wildPokemonController.getWildPokemonForChannel(channelId);
        if (userPokemonController.isPossess(wildPokemon.getPokemonInfo().getName(), userId)) {
            log.info("catching status: User already have this pokemon");
            EmbedBuilder embedBuilder =
                    new EmbedBuilder().setTitle("You already owned this pokemon!");
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        shopController.updateBalanceForUserId(userId, -COST_PER_CATCH);
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle("Catching the " + wildPokemon.getPokemonInfo().getName() + "...")
                        .setDescription(
                                "-"
                                        + COST_PER_CATCH
                                        + " coins, Your Current Balance is "
                                        + shopController.getBalanceForUserId(userId).getBalance());

        event.replyEmbeds(eb.build()).queue();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        // deduct some coins

        Boolean isCaught =
                userPokemonController.AttemptCatch(
                        wildPokemon.getPokemonInfo().getIv().getQuality());

        if (!isCaught) {
            eb = new EmbedBuilder().setTitle("Oops, the pokemon has ran away. Try another Time.");

        } else {
            eb =
                    new EmbedBuilder()
                            .setTitle("Congratulation! The pokemon has added to your pocket!");
            userPokemonController.addPokemon(wildPokemon.getPokemonInfo(), userId);
            wildPokemonController.deletePokemonInfoForChannel(channelId);
        }
        event.getHook().editOriginalEmbeds(eb.build()).queue();
    }
}
