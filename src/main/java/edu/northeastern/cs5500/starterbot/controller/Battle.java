package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import java.util.Random;

public class Battle {

    // key: winner PokemonInfo, Value: battle information
    public Object[] pokemonVersePokemon(PokemonInfo p1, PokemonInfo p2) {
        if (p1.getSpeed() < p2.getSpeed()) return pokemonVersePokemon(p2, p1);

        Integer[] hp = new Integer[2];
        hp[0] = p1.getHp();
        hp[1] = p2.getHp();

        String lineSeparator = System.lineSeparator();
        StringBuilder info = new StringBuilder("Start to Fight!\n");
        int totalSpeed = p1.getSpeed() + p2.getSpeed();
        int speed1 = (int) ((double) p2.getSpeed() / totalSpeed * 10);
        int speed2 = (int) ((double) p1.getSpeed() / totalSpeed * 10);
        int timer = 0;

        while (hp[0] > 0 && hp[1] > 0) {
            String roundInfo;
            if (timer % speed1 == 0) {
                roundInfo = battleHelper(p1, p2, hp, 1, timer);
                info.append(lineSeparator).append("***").append(roundInfo);
            }
            if (timer % speed2 == 0) {

                roundInfo = battleHelper(p2, p1, hp, 0, timer);
                info.append(lineSeparator).append("---").append(roundInfo);
            }
            timer++;
        }
        info.append(lineSeparator).append(lineSeparator).append("Fight is over, ");
        Object[] res = new Object[2];
        if (hp[0] > 0) {
            res[0] = p1;
            info.append(p1.getName() + " win!");
        } else {
            res[0] = p2;
            info.append(p2.getName() + " win!");
        }
        res[1] = info.toString();
        return res;
    }

    private int damageCalculator(int attack, int defense) {
        if (attack < defense) {
            return new Random().nextInt(5);
        }
        int noise = new Random().nextInt(41) - 20;
        return (int) ((attack - defense) * (100.0 + noise) / 100);
    }

    private String battleHelper(
            PokemonInfo p1, PokemonInfo p2, Integer[] hp, int index, int timer) {
        Random rand = new Random();

        String p1Name = p1.getName();
        String p2Name = p2.getName();

        int specialNum = rand.nextInt(10);
        int critNum = rand.nextInt(10);
        int isSpecial = 4;
        int isCrit = 3;
        String round;
        if (specialNum < isSpecial) {
            int damage = damageCalculator(p1.getSpecialAttack(), p2.getSpecialDefense());
            if (critNum < isCrit) {
                round = timer + "s: ";
                round += p1Name + "launch special attack, deals " + damage * 2 + " crit damage. ";
                hp[index] -= damage * 2;
                round += p2Name + "'s hp: " + ((hp[index] > 0) ? hp[index] : 0) + "/" + p2.getHp();
            } else {
                round = timer + "s: ";
                round += p1Name + "launch special attack, deals " + damage + " damage. ";
                hp[index] -= damage;
                round += p2Name + "'s hp: " + ((hp[index] > 0) ? hp[index] : 0) + "/" + p2.getHp();
            }
        } else {
            int damage = damageCalculator(p1.getAttack(), p2.getDefense());
            if (critNum < isCrit) {
                round = timer + "s: ";
                round += p1Name + " attack " + p2Name + ", deals " + damage * 2 + " crit damage. ";
                hp[index] -= damage * 2;
                round += p2Name + "'s hp: " + ((hp[index] > 0) ? hp[index] : 0) + "/" + p2.getHp();
            } else {
                round = timer + "s: ";
                round += p1Name + " attack " + p2Name + ", deals " + damage + " damage. ";
                hp[index] -= damage;
                round += p2Name + "'s hp: " + ((hp[index] > 0) ? hp[index] : 0) + "/" + p2.getHp();
            }
        }
        return round;
    }
}
