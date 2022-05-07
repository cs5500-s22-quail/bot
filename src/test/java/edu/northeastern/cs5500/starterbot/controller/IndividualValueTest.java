package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.IndividualValue;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class IndividualValueTest {
    private static final Integer MAX_IV = 31;
    private static final Integer MIN_IV = 5;

    @Test
    void testRandom() {
        for (int i = 0; i < 500; i++) {
            IndividualValue iv = new IndividualValue();
            if (iv.getHp() < MIN_IV || iv.getHp() > MAX_IV) fail("unexpected hp random number");
            if (iv.getAttack() < MIN_IV || iv.getAttack() > MAX_IV)
                fail("unexpected attack random number");
            if (iv.getDefense() < MIN_IV || iv.getDefense() > MAX_IV)
                fail("unexpected defense random number");
            if (iv.getSpecialAttack() < MIN_IV || iv.getSpecialAttack() > MAX_IV)
                fail("unexpected special attack random number");
            if (iv.getSpecialDefense() < MIN_IV || iv.getSpecialDefense() > MAX_IV)
                fail("unexpected special defense random number");
            if (iv.getSpeed() < MIN_IV || iv.getSpeed() > MAX_IV)
                fail("unexpected speed random number");
        }
    }

    @Test
    void testPercentage() {
        double d1 = (double) MIN_IV / (double) MAX_IV;
        double lowerPercentage = (double) Math.round(d1 * 10000) / 10000 - 0.01;
        double upperPercentage = 1.01;
        for (int i = 0; i < 500; i++) {
            IndividualValue iv = new IndividualValue();
            if (iv.getIVPercentage() <= lowerPercentage) fail("percentage is lower than expected");
            if (iv.getIVPercentage() >= upperPercentage)
                fail(
                        "percentage is higher than expected, and the value is "
                                + iv.getIVPercentage());
        }
    }

    @Test
    void testPercentageFormat() {
        for (int i = 0; i < 500; i++) {
            IndividualValue iv = new IndividualValue();
            String format = iv.getIVPercentageFormat();
            if (format.charAt(2) != '.' || format.charAt(format.length() - 1) != '%')
                fail("Invalid format, Expected is xx.xx%, but actual is " + format);
        }
    }

    @Test
    void testGetQuality() {

        Set<String> set = new HashSet<>();
        set.add("Legendary");
        set.add("Epic");
        set.add("Superior");
        set.add("Good");
        for (int i = 0; i < 100; i++) {
            IndividualValue individualValue = new IndividualValue();
            assertTrue(set.contains(individualValue.getQualityName()));
        }
    }

    @Test
    void testData() {
        IndividualValue individualValue = new IndividualValue();
        IndividualValue individualValue1 = new IndividualValue();
        assertThat(individualValue).isNotEqualTo(individualValue1);

        assertThat(individualValue.toString()).isNotNull();
        assertThat(individualValue.getHp()).isNotNull();
        assertThat(individualValue.getSpeed()).isNotNull();
        assertThat(individualValue.getAttack()).isNotNull();
        assertThat(individualValue.getDefense()).isNotNull();
        assertThat(individualValue.getSpecialAttack()).isNotNull();
        assertThat(individualValue.getSpecialDefense()).isNotNull();

        individualValue.setAttack(1);
        individualValue.setDefense(1);
        individualValue.setSpecialAttack(1);
        individualValue.setSpecialDefense(1);
        individualValue.setHp(1);
        individualValue.setSpeed(1);
    }
}
