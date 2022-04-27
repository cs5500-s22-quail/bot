package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.service.PokemonService;
import javax.inject.Inject;
import lombok.Data;

@Data
public class MultiUserController {

    @Inject UserPokemonController userPokemonController;

    PokemonInfo p1;
    PokemonInfo p2;

    @Inject
    public MultiUserController() {
        this.p1 = new PokemonService().fromID(25);
        this.p2 = new PokemonService().fromID(23);
    }

    public void setPokemons(PokemonInfo pokemon1, PokemonInfo pokemon2) {
        this.p1 = pokemon1;
        this.p2 = pokemon2;
    }
}
