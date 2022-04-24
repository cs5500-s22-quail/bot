package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.*;

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
    public GenericRepository<UserPokemon> provideUserPokemonRepository(
            MongoDBRepository<UserPokemon> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<UserBalance> provideShopRepository(
            MongoDBRepository<UserBalance> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<BattleRequest> provideBattleRequestRepository(
            MongoDBRepository<BattleRequest> repository) {
        return repository;
    }

    @Provides
    public Class<BattleRequest> provideBattleRequest() {
        return BattleRequest.class;
    }

    @Provides
    public Class<UserPreference> provideUserPreference() {
        return UserPreference.class;
    }

    @Provides
    public Class<WildPokemon> provideWildPokemon() {
        return WildPokemon.class;
    }

    @Provides
    public Class<UserPokemon> provideUserPokemon() {
        return UserPokemon.class;
    }

    @Provides
    public Class<UserBalance> provideUserBalance() {
        return UserBalance.class;
    }
}
