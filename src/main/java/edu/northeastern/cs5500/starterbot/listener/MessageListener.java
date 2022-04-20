package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.command.Command;
import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class MessageListener extends ListenerAdapter {
    @Inject WildPokemonController wildPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject EmbedBuilderGenerator embedBuilderGenerator;

    @Inject Set<Command> commands;

    @Inject
    public MessageListener() {
        super();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        for (Command command : commands) {
            if (command.getName().equals(event.getName())) {
                command.onEvent(event);
                return;
            }
        }
    }

    public Collection<CommandData> allCommandData() {
        return commands.stream().map(Command::getCommandData).collect(Collectors.toList());
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("moneyMagic")) {
            event.reply("MoneyMagic feature is developing...").queue(); // TODO: finishing...
        } else if (event.getComponentId().equals("fight")) {
            EmbedBuilder embedBuilder = embedBuilderGenerator.getFightPokemonEmbeds();
            // TODO: for now, event ends here by showing the wild pokemon image
            // Once changes for saving and calling existing pokemon are done,
            // needs to add logics for comparing level and showing the results
            event.replyEmbeds(embedBuilder.build()).queue();
        }
    }
}
