package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonInfo;
import edu.northeastern.cs5500.starterbot.model.UserPokemon;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;

public class UserPokemonController {
    GenericRepository<UserPokemon> userPokemonRepository;

    @Inject
    public UserPokemonController(GenericRepository<UserPokemon> userPokemonRepository) {
        this.userPokemonRepository = userPokemonRepository;

        if (this.userPokemonRepository.count() == 0) {
            UserPokemon userPokemon = new UserPokemon();
            userPokemon.setUserID("7777");
            userPokemon.setPokemonTeam(new ArrayList<>());
            this.userPokemonRepository.add(userPokemon);
        }
    }

    @Nonnull
    public UserPokemon getUserPokemonForMemberID(String userID) {
        Collection<UserPokemon> userPokemons = this.userPokemonRepository.getAll();
        for (UserPokemon userPokemon : userPokemons) {
            if (userPokemon.getUserID().equals(userID)) return userPokemon;
        }
        UserPokemon userPokemon = new UserPokemon();
        userPokemon.setUserID(userID);
        userPokemon.setPokemonTeam(new ArrayList<>());
        this.userPokemonRepository.add(userPokemon);
        return userPokemon;
    }

    public void addPokemon(PokemonInfo pokemonInfo, String userID) {
        if (this.isPossess(pokemonInfo.getName(), userID)) return;
        UserPokemon userPokemon = this.getUserPokemonForMemberID(userID);
        ArrayList<PokemonInfo> list = userPokemon.getPokemonTeam();
        list.add(pokemonInfo);
        if (userPokemon.getCarriedPokemon() == null) {
            userPokemon.setCarriedPokemon(pokemonInfo);
        }
        this.userPokemonRepository.update(userPokemon);
    }

    public void updateCarriedPokemonForMemberID(String userID, Integer index) {
        UserPokemon userPokemon = this.getUserPokemonForMemberID(userID);
        userPokemon.setCarriedPokemon(userPokemon.getPokemonTeam().get(index - 1));
    }

    @Nonnull
    public Boolean hasCarriedPokemon(String userID) {
        return this.getUserPokemonForMemberID(userID).getCarriedPokemon() != null;
    }

    @Nonnull
    public Boolean isPossess(String speciesName, String userID) {
        UserPokemon userPokemon = this.getUserPokemonForMemberID(userID);
        if (userPokemon.getPokemonTeam() == null) userPokemon.setPokemonTeam(new ArrayList<>());
        for (PokemonInfo pokemonInfo : userPokemon.getPokemonTeam()) {
            if (pokemonInfo.getName().equals(speciesName)) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Nonnull
    public Boolean AttemptCatch(Quality quality) {
        Random random = new Random();
        int attempt = random.nextInt(10);
        if (attempt < this.probability(quality)) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Nonnull
    public Integer probability(Quality quality) {
        switch (quality) {
            case RED:
                return 2;
            case PURPLE:
                return 3;
            case BLUE:
                return 5;
            case GREEN:
                return 7;
            default:
                return 10;
        }
    }

    public void levelUp(UserPokemon userPokemon) {
        PokemonInfo carriedPokemon = userPokemon.getCarriedPokemon();
        carriedPokemon.setLevel(carriedPokemon.getLevel() + 1);

        for (PokemonInfo pokemonInfo : userPokemon.getPokemonTeam()) {
            System.out.println("test " + pokemonInfo.getName());
            if (pokemonInfo.getName().equals(carriedPokemon.getName())) {
                pokemonInfo.setLevel(carriedPokemon.getLevel());
                System.out.println("Find the pokemon in team");
                break;
            }
        }
        userPokemonRepository.update(userPokemon);
        // TODO IV update
    }
}
