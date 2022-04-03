package edu.northeastern.cs5500.starterbot.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.iamgio.pokedex.connection.HttpConnection;
import eu.iamgio.pokedex.exception.PokedexException;

/**
 * The JAVA API we use does not process certain json objects that we might use, This class is a list
 * of extended functions to get data we need on https://pokeapi.co/
 */
public class ExtendedPokemon {
    JsonObject pokemonJson;

    public ExtendedPokemon(String name) {
        this.pokemonJson = this.fromName(name);
    }

    public ExtendedPokemon(Number ID) {
        this.pokemonJson = this.fromID(ID);
    }

    private JsonObject fromName(String name) {
        JsonObject json;
        try {
            json = (new HttpConnection("pokemon/" + name + "/")).getJson();
        } catch (RuntimeException var10) {
            throw new PokedexException("Could not find Pokémon with name/ID " + name);
        }
        return json;
    }

    private JsonObject fromID(Number ID) throws PokedexException {
        return fromName(String.valueOf(ID));
    }

    /**
     * This is according to the https://pokeapi.co/, we can add more getter() method to manually get
     * respective data.
     *
     * @return return the url of the official artworks pictures of a respective Pokemon
     */
    public String getOfficialArtworkUrl() {

        JsonElement urlElement =
                this.pokemonJson
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("other")
                        .getAsJsonObject("official-artwork")
                        .get("front_default");
        return urlElement.getAsString();
    }
}
