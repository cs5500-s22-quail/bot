package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class TrainCommandTest {

    @Test
    void getName() {
        TrainCommand trainCommand = new TrainCommand();
        String name = trainCommand.getName();
        CommandData commandData = trainCommand.getCommandData();
        assertThat(name).isEqualTo(commandData.getName());
    }
}
