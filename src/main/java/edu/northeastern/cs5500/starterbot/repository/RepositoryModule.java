package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.PokemonIV;
import edu.northeastern.cs5500.starterbot.model.UserPreference;
import edu.northeastern.cs5500.starterbot.model.WildPokemon;

@Module
public class RepositoryModule {
    // NOTE: You can use the following lines if you'd like to store objects in memory.
    // NOTE: The presence of commented-out code in your project *will* result in a lowered grade.
    // @Provides
    // public GenericRepository<UserPreference> provideUserPreferencesRepository(
    //         InMemoryRepository<UserPreference> repository) {
    //     return repository;
    // }

    @Provides
    public GenericRepository<UserPreference> provideUserPreferencesRepository(
            MongoDBRepository<UserPreference> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<WildPokemon> provideWildPokemonRepository(
            MongoDBRepository<WildPokemon> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<PokemonIV> providePokemonIVRepository(
            MongoDBRepository<PokemonIV> repository) {
        return repository;
    }

    @Provides
    public Class<PokemonIV> providePokemonIV() {
        return PokemonIV.class;
    }

    @Provides
    public Class<UserPreference> provideUserPreference() {
        return UserPreference.class;
    }

    @Provides
    public Class<WildPokemon> provideWildPokemon() {
        return WildPokemon.class;
    }
}
