package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.BattleController;
import edu.northeastern.cs5500.starterbot.controller.DisplayController;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import javax.inject.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public class EvenReplyHandler {

    @Inject DisplayController displayController;

    @Inject BattleController battleController;

    @Inject
    EvenReplyHandler() {}

    public void battleUI(PokemonInfo p1, PokemonInfo p2, ButtonClickEvent event) {
        // Initial Status
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb1 = displayController.pokemonStatus(p1);
        EmbedBuilder eb2 = displayController.pokemonStatus(p2);
        EmbedBuilder eb3 = new EmbedBuilder().setTitle("Start to Fight!");
        mb.setEmbeds(eb1.build(), eb2.build(), eb3.build());
        event.reply(mb.build()).queue();

        // Battling
        Integer[] hp = new Integer[2];
        hp[0] = p1.getHp();
        hp[1] = p2.getHp();

        String lineSeparator = System.lineSeparator();

        StringBuilder info = new StringBuilder();
        int totalSpeed = p1.getSpeed() + p2.getSpeed();
        int speed1 = (int) ((double) p2.getSpeed() / totalSpeed * 10);
        int speed2 = (int) ((double) p1.getSpeed() / totalSpeed * 10);
        int timer = 0;
        int count = 0;
        int displayInterval = 3;
        while (hp[0] > 0 && hp[1] > 0) {
            String roundInfo;
            if (timer % speed1 == 0) {
                roundInfo = this.battleController.battleHelper(p1, p2, hp, 1, timer);
                info.append(lineSeparator).append(roundInfo);
                count++;
                if (count != 0 && count % displayInterval == 0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    eb1.setTitle("HP: " + hp[0] + "/" + p1.getHp());
                    eb2.setTitle("HP: " + hp[1] + "/" + p2.getHp());
                    eb3.setDescription(info.toString());
                    event.getHook()
                            .editOriginalEmbeds(eb1.build(), eb2.build(), eb3.build())
                            .queue();
                    info.delete(0, info.length() - 1);
                }
            }
            if (hp[1] < 0 || hp[0] < 0) break;

            if (timer % speed2 == 0) {

                roundInfo = this.battleController.battleHelper(p2, p1, hp, 0, timer);
                info.append(lineSeparator).append(roundInfo);
                count++;
                if (count != 0 && count % displayInterval == 0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    eb1.setTitle("HP: " + hp[0] + "/" + p1.getHp());
                    eb2.setTitle("HP: " + hp[1] + "/" + p2.getHp());
                    eb3.setDescription(info.toString());
                    event.getHook()
                            .editOriginalEmbeds(eb1.build(), eb2.build(), eb3.build())
                            .queue();
                    info.delete(0, info.length() - 1);
                }
            }
            timer++;
        }

        // battle ends
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        eb3.setDescription("");

        if (hp[0] > 0) {
            eb3.setTitle("Battle is over, " + p1.getName() + " win!");
        } else {
            eb3.setTitle("Battle is over, " + p2.getName() + " win!");
        }
        eb1.setTitle("HP: " + hp[0] + "/" + p1.getHp());
        eb2.setTitle("HP: " + hp[1] + "/" + p2.getHp());
        event.getHook().editOriginalEmbeds(eb1.build(), eb2.build(), eb3.build()).queue();
    }
}
