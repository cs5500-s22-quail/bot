package edu.northeastern.cs5500.starterbot.controller;

import java.util.Random;

public class PokemonGenerator {
  private static final int MAX_POKEMON_ID = 898;
  private static final int MAX_POKEMON_LEVEL = 50;

  Integer randomPokemonID;
  ExtendedPokemon extendedPokemon;
  Integer level;

  //constructor:
  public PokemonGenerator(int level) {
    Random rand = new Random();
    this.randomPokemonID = rand.nextInt(MAX_POKEMON_ID + 1);
    this.extendedPokemon = ExtendedPokemon.fromID(this.randomPokemonID);
    this.level = level;
  }

  public PokemonGenerator() {
    Random rand = new Random();
    this.randomPokemonID = rand.nextInt(MAX_POKEMON_ID + 1);
    this.extendedPokemon = ExtendedPokemon.fromID(this.randomPokemonID);
    this.level = rand.nextInt(MAX_POKEMON_LEVEL + 1);
  }
}
