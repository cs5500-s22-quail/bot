package edu.northeastern.cs5500.starterbot.command;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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
        return new CommandData(getName(), "Train your pokemon walking with you")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "The bot will reply to your command with the provided text")
                                .setRequired(false));
    }

    @Override
    public void onEvent(CommandInteraction event) {
        log.info("event: /train");
        event.reply("Click the button to say hello")
                .addActionRow(
                        Button.primary("moneyMagic", "MoneyMagic"), // Button with only a label
                        Button.danger(
                                        "fight", "Fight"
                                        //
                                        // Emoji.fromMarkdown("<:minn:245267426227388416> Emoji")

                                        // Button with only an emoji
                                        )
                                .withEmoji(Emoji.fromUnicode("U+2694")))
                .queue();
    }

    //    @Override
    //    public void onButtonClick(ButtonClickEvent event) {
    //        if (event.getComponentId().equals("hello")) {
    //            event.reply("Hello :)").queue();
    //        }
    //    }
}
