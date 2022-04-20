package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.listener.EmbedBuilderGenerator;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class TrainCommand implements Command, ButtonClickHandler {
    @Inject EmbedBuilderGenerator embedBuilderGenerator;

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
        log.info("event: /train");
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
            event.reply("MoneyMagic feature is developing...").queue(); // TODO: finishing...
        } else if (event.getComponentId().equals("train:fight")) {
            EmbedBuilder embedBuilder = embedBuilderGenerator.getFightPokemonEmbeds();
            // TODO: for now, event ends here by showing the wild pokemon image
            // Once changes for saving and calling existing pokemon are done,
            // needs to add logics for comparing level and showing the results
            event.replyEmbeds(embedBuilder.build()).queue();
        }

        event.reply(event.getButton().getLabel()).queue();
    }
}
