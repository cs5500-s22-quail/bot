package edu.northeastern.cs5500.starterbot.command;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

@Singleton
@Slf4j
public class TrainCommand implements Command {

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
        event.reply("Click the button to level up your pokemon")
                .addActionRow(
                        Button.primary("moneyMagic", "MoneyMagic"), // Button with only a label
                        Button.danger("fight", "Fight").withEmoji(Emoji.fromUnicode("U+2694")))
                .queue();
    }
}
