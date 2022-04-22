package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.command.ButtonClickHandler;
import edu.northeastern.cs5500.starterbot.command.Command;
import edu.northeastern.cs5500.starterbot.command.SelectionMenuHandler;
import edu.northeastern.cs5500.starterbot.controller.PokemonGenerator;
import edu.northeastern.cs5500.starterbot.controller.WildPokemonController;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Slf4j
public class MessageListener extends ListenerAdapter {
    @Inject WildPokemonController wildPokemonController;
    @Inject PokemonGenerator pokemonGenerator;
    @Inject AllPokemonController embedBuilderGenerator;

    @Inject Set<Command> commands;
    @Inject Set<SelectionMenuHandler> selectionMenus;
    @Inject Set<ButtonClickHandler> buttons;

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
    public void onSelectionMenu(@Nonnull SelectionMenuEvent event) {
        log.info("onSelectionMenu: {}", event.getComponent().getId());
        String handlerName = event.getComponent().getId();

        for (SelectionMenuHandler selectionMenuHandler : selectionMenus) {
            log.info("selection menu is: " + selectionMenuHandler.getName());
            if (selectionMenuHandler.getName().equals(handlerName)) {
                selectionMenuHandler.onSelectionMenu(event);
                return;
            }
        }

        log.error("Unknown button handler: {}", handlerName);
    }

    @Override
    public void onButtonClick(@Nonnull ButtonClickEvent event) {
        log.info("onButtonClick: {}", event.getButton().getId());
        String id = event.getButton().getId();
        String handlerName = id.split(":", 2)[0];

        for (ButtonClickHandler buttonClickHandler : buttons) {
            if (buttonClickHandler.getName().equals(handlerName)) {
                buttonClickHandler.onButtonClick(event);
                return;
            }
        }

        log.error("Unknown button handler: {}", handlerName);
    }
}
