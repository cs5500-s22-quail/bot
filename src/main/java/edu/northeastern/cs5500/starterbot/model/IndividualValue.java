package edu.northeastern.cs5500.starterbot.model;

import edu.northeastern.cs5500.starterbot.controller.Quality;
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

    private static final Integer MAX_IV = 31;
    private static final Integer MAX_POSSIBLE_IV = 30;
    private static final Integer MIN_POSSIBLE_IV = 5;
    private static final Integer NUMBER_OF_STATS = 6;
    private static final Integer DECIMAL_PLACES = 2;

    public IndividualValue() {
        this.hp = ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1);
        this.attack = ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1);
        this.defense = ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1);
        this.specialAttack =
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1);
        this.specialDefense =
                ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1);
        this.speed = ThreadLocalRandom.current().nextInt(MIN_POSSIBLE_IV, MAX_POSSIBLE_IV + 1);
    }

    public String getQualityName() {
        Quality quality = this.getQuality();
        switch (quality) {
            case RED:
                return "Legendary";
            case PURPLE:
                return "Epic";
            case BLUE:
                return "Superior";
            case GREEN:
                return "Good";
            default:
                return "";
        }
    }

    public Quality getQuality() {
        return this.setQuality(this.getIVPercentage() * 100);
    }

    /**
     * 0.3443123 -> 0.3443
     *
     * @return
     */
    public Double getIVPercentage() {
        return this.calculatePercentage(hp, attack, defense, specialAttack, specialDefense, speed);
    }

    /** @return the format of IV Percentage; */
    public String getIVPercentageFormat() {
        return this.formatPercentage(this.getIVPercentage());
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

    private Quality setQuality(Double ivpercentage) {
        int value = ivpercentage.intValue();
        if (value < 45) return Quality.GREEN;
        if (value < 65) return Quality.BLUE;
        if (value < 85) return Quality.PURPLE;
        return Quality.RED;
    }

    private String formatPercentage(Double value) {
        NumberFormat format = NumberFormat.getPercentInstance(Locale.US);
        format.setMinimumFractionDigits(DECIMAL_PLACES);
        return format.format(value);
    }
}
