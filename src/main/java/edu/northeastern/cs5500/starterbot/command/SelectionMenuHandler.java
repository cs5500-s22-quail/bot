package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;

public interface SelectionMenuHandler {
    @Nonnull
    public String getName();

    public void onSelectionMenu(@Nonnull SelectionMenuEvent event);
}
