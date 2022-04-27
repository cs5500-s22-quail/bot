package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class GiftCommandTest {

    private static String discordUserId = "940390391214141450";

    @Test
    void testGetCommandData() {
        GiftCommand giftCommand = new GiftCommand();
        String name = giftCommand.getName();
        CommandData commandData = giftCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }
}
