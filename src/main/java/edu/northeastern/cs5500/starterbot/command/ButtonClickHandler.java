package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface ButtonClickHandler {
    @Nonnull
    public String getName();

    public void onButtonClick(@Nonnull ButtonClickEvent event);
}
