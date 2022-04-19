package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;

@Data
public class PokemonInfo {
    String name;
    IndividualValue iv;
    Integer level;

    // Official website
    String officialArtworkUrl;
    // Stat
    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;
}
