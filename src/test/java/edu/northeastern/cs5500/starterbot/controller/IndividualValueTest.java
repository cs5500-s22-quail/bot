package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.IndividualValue;
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
}
