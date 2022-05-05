package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class FishCommandTest {

    @Test
    void testNameMatchesData() {
        FishCommand fishCommand = new FishCommand();
        String name = fishCommand.getName();
        CommandData commandData = fishCommand.getCommandData();
        assertThat(name).isEqualTo(commandData.getName());
    }
}
