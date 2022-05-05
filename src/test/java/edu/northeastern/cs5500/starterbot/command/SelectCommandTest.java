package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class SelectCommandTest {

    @Test
    void getName() {
        SelectCommand selectCommand = new SelectCommand();
        String name = selectCommand.getName();
        CommandData commandData = selectCommand.getCommandData();
        assertThat(name).isEqualTo(commandData.getName());
    }
}
