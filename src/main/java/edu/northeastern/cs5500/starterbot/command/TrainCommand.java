package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.BattleController;
import edu.northeastern.cs5500.starterbot.controller.DisplayController;
import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.controller.ShopController;
import edu.northeastern.cs5500.starterbot.controller.TrainController;
import edu.northeastern.cs5500.starterbot.controller.UserPokemonController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class TrainCommand implements Command, ButtonClickHandler {
    int price;
    EmbedBuilder eb;
    SlashCommandEvent slashCommandEvent;
    ButtonClickEvent moneyMagicEvent;
    //    @Inject EmbedBuilderGenerator embedBuilderGenerator;
    @Inject UserPokemonController userPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject TrainController trainController;
    @Inject ShopController shopController;
    @Inject DisplayController displayController;
    @Inject BattleController battleController;

    @Inject
    public TrainCommand() {}

    @Override
    public String getName() {
        return "train";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Train your pokemon walking with you");
    }

    @Override
    public void onEvent(CommandInteraction event) {
        this.slashCommandEvent = (SlashCommandEvent) event;
        log.info("event: /train");
        eb = new EmbedBuilder();
        event.reply("How do you want to level up your pokemon?")
                .addActionRow(
                        Button.primary(
                                "train:moneyMagic", "MoneyMagic"), // Button with only a label
                        Button.danger("train:fight", "Fight")
                                .withEmoji(Emoji.fromUnicode("U+2694")))
                .queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("train:moneyMagic")) {
            slashCommandEvent
                    .getHook()
                    .editOriginal("MoneyMagic is being loaded...")
                    .setActionRows()
                    .queue();
            moneyMagicEvent = clickMoneyMagic(event);
        } else if (event.getComponentId().equals("train:fight")) {
            slashCommandEvent
                    .getHook()
                    .editOriginal("Fight is being loaded...")
                    .setActionRows()
                    .queue();
            clickFight(event);
        } else if (event.getComponentId().equals("train:moneyLevelUp")) {
            clickMoneyLevelUp(event, moneyMagicEvent);
        } else if (event.getComponentId().equals("train:moneyCancel")) {
            clickMoneyCancel(event, moneyMagicEvent);
        }
    }

    public ButtonClickEvent clickMoneyMagic(ButtonClickEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        String userId = event.getUser().getId();
        int userBalance = shopController.getBalanceForUserId(userId).getBalance();
        PokemonInfo pokemonInfo =
                userPokemonController.getUserPokemonForMemberID(userId).getCarriedPokemon();
        int userPokemonLevel = pokemonInfo.getLevel();
        int toLevel = userPokemonLevel + 1;
        //        int price = 100 * (int) Math.pow(2, userPokemonLevel / 10);
        int basePrice = 10;
        price = basePrice * (int) Math.pow(2, userPokemonLevel / 10);
        event.replyEmbeds(
                        eb.setTitle(
                                        "Do you want to cost "
                                                + price
                                                + " coins to level up your pokemon to Level "
                                                + toLevel
                                                + "?")
                                .setFooter("Your balance: " + userBalance + " coins")
                                .build())
                .addActionRow(
                        Button.success(
                                "train:moneyLevelUp", "Level up"), // Button with only a label
                        Button.secondary("train:moneyCancel", "Cancel"))
                .queue();
        return event;
    }

    public void clickFight(ButtonClickEvent event) {
        // Step1: random pokemon will show up to fight
        WildPokemon fightPokemon = pokemonGenerator.getWildPokemon();
        String userId = event.getUser().getId();
        EmbedBuilder embedBuilder = trainController.getFightPokemonEmbeds(fightPokemon);
        event.replyEmbeds(embedBuilder.build()).queue();

        // Step2: random pokemon fighting with user's pokemon
        UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberID(userId);
        PokemonInfo userPokemonInfo = userPokemon.getCarriedPokemon();
        int levelBefore = userPokemonInfo.getLevel();
        battleController.battleUI(
                fightPokemon.getPokemonInfo(), userPokemon.getCarriedPokemon(), event);
        embedBuilder =
                trainController.getFightResultEmbeds(
                        embedBuilder, fightPokemon.getPokemonInfo(), userPokemon);
        // TODO show the fight process here:
        //        trainController.showFightProcess(event, fightPokemon.getPokemonInfo(),
        // userPokemon);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        int levelAfter = userPokemonInfo.getLevel();

        // Step3:If the user win, show the pokemon info after updating
        if (levelBefore < levelAfter) {
            embedBuilder = trainController.getLevelUpEmbeds(embedBuilder, userPokemonInfo);
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        }
    }

    public void clickMoneyLevelUp(ButtonClickEvent event, ButtonClickEvent lastEvent) {
        // delete the price from the user balance -> succeed -> level up
        //                                       -> faild  -> send message
        EmbedBuilder eb = new EmbedBuilder();
        String userId = event.getUser().getId();
        try {
            shopController.updateBalanceForUserId(userId, -price);
            UserPokemon userPokemon = userPokemonController.getUserPokemonForMemberID(userId);
            userPokemonController.levelUp(userPokemon);
            lastEvent
                    .getHook()
                    .editOriginalEmbeds(eb.setTitle("Leveling up...").build())
                    .setActionRows()
                    .queue();
            PokemonInfo pokemonInfoLevelup = userPokemon.getCarriedPokemon();
            //            String description = displayController.PokemonInfoUI(pokemonInfoLevelup);
            eb.setTitle(":tada:Your pokemon has been up-leveled")
                    .setDescription(
                            "Level "
                                    + pokemonInfoLevelup.getLevel()
                                    + " "
                                    + pokemonInfoLevelup.getName()
                                    + "\n"
                                    + displayController.PokemonInfoUI(pokemonInfoLevelup))
                    .setFooter(
                            "Your current balance: "
                                    + shopController.getBalanceForUserId(userId).getBalance()
                                    + " coins")
                    .setImage(pokemonInfoLevelup.getOfficialArtworkUrl());
            event.replyEmbeds(eb.build()).queue();

        } catch (Exception e) {
            event.reply("Ops! Your balance isn't enough. Go fishing to earn more coins.");
        }
    }

    public void clickMoneyCancel(ButtonClickEvent event, ButtonClickEvent lastEvent) {
        EmbedBuilder eb = new EmbedBuilder();
        lastEvent
                .getHook()
                .editOriginalEmbeds(eb.setTitle("Canceled").build())
                .setActionRows() // enable the button
                .queue();
    }
}
