package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public interface PrivateMessageReceivedHandler {

    @Nonnull
    public String getName();

    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event);
}
