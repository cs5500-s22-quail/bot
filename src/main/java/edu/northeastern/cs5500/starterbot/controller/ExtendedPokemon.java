package edu.northeastern.cs5500.starterbot.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.iamgio.pokedex.connection.HttpConnection;
import eu.iamgio.pokedex.exception.PokedexException;
import javax.annotation.Nonnull;
import lombok.Data;

/**
 * The JAVA API we use does not process certain json objects that we might use, This class is a list
 * of extended functions to get data we need on https://pokeapi.co/
 */
@Data
public class ExtendedPokemon {
    JsonObject pokemonJson;
    private static final int MAX_POKEMON_ID = 898;

    private ExtendedPokemon(JsonObject pokemonJson) {
        this.pokemonJson = pokemonJson;
    }

    public static ExtendedPokemon fromName(String name) {
        JsonObject json;
        try {
            json = (new HttpConnection("pokemon/" + name + "/")).getJson();
        } catch (RuntimeException var10) {
            throw new PokedexException("Could not find Pokémon with name/ID " + name);
        }
        ExtendedPokemon extendedPokemon = new ExtendedPokemon(json);

        return extendedPokemon;
    }

    public static ExtendedPokemon fromID(Number ID) throws PokedexException {
        return fromName(String.valueOf(ID));
    }

    /**
     * This is according to the https://pokeapi.co/, we can add more getter() method to manually get
     * respective data.
     *
     * @return return the url of the official artworks pictures of a respective Pokemon
     */
    @Nonnull
    public String getOfficialArtworkUrl() {

        JsonElement jsonElement =
                this.pokemonJson
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("other")
                        .getAsJsonObject("official-artwork")
                        .get("front_default");
        return jsonElement.getAsString();
    }

    @Nonnull
    public String getSpeciesName() {
        JsonElement jsonElement = this.pokemonJson.getAsJsonObject("species").get("name");
        return jsonElement.getAsString();
    }
}
