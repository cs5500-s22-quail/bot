package edu.northeastern.cs5500.starterbot.controller;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class ProfileControllerTest {

    ProfileController profileController;

    @BeforeEach
    void setUp() {
        this.profileController = new ProfileController();
    }

    @Test
    public void testGetProfile() {
        this.profileController.getProfile("testDiscordId", "www.google.com", "Michael Gary Scott");
    }
}
