package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class PokemonIV implements Model {
    ObjectId id;

    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;
    Double IVPercentage;
    String IVPercentageFormat;
}
