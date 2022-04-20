package edu.northeastern.cs5500.starterbot.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.northeastern.cs5500.starterbot.model.IndividualValue;
import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import eu.iamgio.pokedex.connection.HttpConnection;
import eu.iamgio.pokedex.exception.PokedexException;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * The JAVA API we use does not process certain json objects that we might use, This class is a list
 * of extended functions to get data we need on https://pokeapi.co/
 */
@Singleton
@Slf4j
public class PokemonService implements Service {

    private static final int MAX_POKEMON_ID = 898;
    private static final int MAX_POKEMON_LEVEL = 50;

    @Inject
    public PokemonService() {}

    // will always get a level1 Pokemon
    public PokemonInfo fromName(String name) {
        return fromNameWithRandomLevel(name, false);
    }

    public PokemonInfo fromNameWithRandomLevel(String name, boolean randomLevel) {
        JsonObject json;
        try {
            json = (new HttpConnection("pokemon/" + name + "/")).getJson();
        } catch (RuntimeException var10) {
            throw new PokedexException("Could not find Pokémon with name/ID " + name);
        }

        PokemonInfo pokemonInfo = new PokemonInfo();

        pokemonInfo.setName(this.getName(json));
        if (randomLevel) {
            int rand = (int) (Math.random() * MAX_POKEMON_LEVEL) + 1;
            pokemonInfo.setLevel(rand);
        } else {
            pokemonInfo.setLevel(1);
        }
        pokemonInfo.setIv(new IndividualValue());
        pokemonInfo.setOfficialArtworkUrl(this.getOfficialArtworkUrl(json));

        // stats
        pokemonInfo.setHp(this.getStatHp(json));
        pokemonInfo.setAttack(this.getStatAttack(json));
        pokemonInfo.setDefense(this.getStatDefense(json));
        pokemonInfo.setSpecialAttack(this.getStatSpecialAttack(json));
        pokemonInfo.setSpecialDefense(this.getStatSpecialDefense(json));
        pokemonInfo.setSpeed(this.getStatSpeed(json));

        return pokemonInfo;
    }

    public PokemonInfo fromID(Number ID) throws PokedexException {
        return this.fromName(String.valueOf(ID));
    }

    public PokemonInfo fromIDWithRandomLevel(Number ID) throws PokedexException {
        return this.fromNameWithRandomLevel(String.valueOf(ID), true);
    }

    /**
     * This is according to the https://pokeapi.co/, we can add more getter() method to manually get
     * respective data.
     *
     * @return return the url of the official artworks pictures of a respective Pokemon
     */
    @Nonnull
    private String getOfficialArtworkUrl(JsonObject pokemonJson) {

        JsonElement jsonElement =
                pokemonJson
                        .getAsJsonObject("sprites")
                        .getAsJsonObject("other")
                        .getAsJsonObject("official-artwork")
                        .get("front_default");
        return jsonElement.getAsString();
    }

    @Nonnull
    private String getName(JsonObject pokemonJson) {
        JsonElement jsonElement = pokemonJson.getAsJsonObject("species").get("name");
        return jsonElement.getAsString();
    }

    @Nonnull
    private Integer getStat(Integer id, JsonObject pokemonJson) {
        JsonArray jsonArray = pokemonJson.getAsJsonArray("stats");
        JsonElement jsonElement = jsonArray.get(id).getAsJsonObject().get("base_stat");
        return jsonElement.getAsInt();
    }

    @Nonnull
    private Integer getStatHp(JsonObject pokemonJson) {
        return this.getStat(0, pokemonJson);
    }

    @Nonnull
    private Integer getStatAttack(JsonObject pokemonJson) {
        return this.getStat(1, pokemonJson);
    }

    @Nonnull
    private Integer getStatDefense(JsonObject pokemonJson) {
        return this.getStat(2, pokemonJson);
    }

    @Nonnull
    private Integer getStatSpecialAttack(JsonObject pokemonJson) {
        return this.getStat(3, pokemonJson);
    }

    @Nonnull
    private Integer getStatSpecialDefense(JsonObject pokemonJson) {
        return this.getStat(4, pokemonJson);
    }

    @Nonnull
    private Integer getStatSpeed(JsonObject pokemonJson) {
        return this.getStat(5, pokemonJson);
    }

    /** Initialize the service. */
    @Override
    public void register() {
        log.info("PokemonService > register");
    }
}
