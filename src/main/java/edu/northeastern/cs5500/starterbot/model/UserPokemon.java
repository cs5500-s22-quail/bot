package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UserPokemon implements Model {
    ObjectId id;

    ArrayList<WildPokemon> PokemonID;

    String userID;
}
