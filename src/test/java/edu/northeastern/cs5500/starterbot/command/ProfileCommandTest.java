package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class ProfileCommandTest {
    @Test
    void testNameMatchesData() {
        ProfileCommand profileCommand = new ProfileCommand();
        String name = profileCommand.getName();
        CommandData commandData = profileCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }
}
