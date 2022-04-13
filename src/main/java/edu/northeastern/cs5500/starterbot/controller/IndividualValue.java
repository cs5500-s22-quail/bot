package edu.northeastern.cs5500.starterbot.controller;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Data;

@Data
public class IndividualValue {

    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;
    Double IVPercentage;
    String IVPercentageFormat;

    private static final Integer MAX_IV = 31;
    private static final Integer MAX_POSSIBLE_IV = 30;
    private static final Integer MIN_POSSIBLE_IV = 5;
    private static final Integer NUMBER_OF_STATS = 6;
    private static final Integer DECIMAL_PLACES = 2;

    private IndividualValue(
            Integer hp,
            Integer attack,
            Integer defense,
            Integer specialAttack,
            Integer specialDefense,
            Integer speed) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.IVPercentage =
                this.calculatePercentage(hp, attack, defense, specialAttack, specialDefense, speed);
        this.IVPercentageFormat = this.formatPercentage(this.IVPercentage);
    }

    /** This double value will have 2 decimal places. */
    private Double calculatePercentage(
            Integer hp,
            Integer attack,
            Integer defense,
            Integer specialAttack,
            Integer specialDefense,
            Integer speed) {
        Double percent =
                (hp + attack + defense + specialAttack + specialDefense + speed)
                        / (double) (MAX_IV * NUMBER_OF_STATS);
        percent = (double) Math.round(percent * 10000) / 10000;
        return percent;
    }

    private String formatPercentage(Double value) {
        NumberFormat format = NumberFormat.getPercentInstance(Locale.US);
        format.setMinimumFractionDigits(DECIMAL_PLACES);
        return format.format(value);
    }

    public static IndividualValue generateIV() {
        return new IndividualValue(
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1),
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1),
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1),
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1),
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1),
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1));
    }
}
