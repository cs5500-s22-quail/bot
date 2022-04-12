package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class CommandModule {

    @Provides
    @IntoSet
    public Command provideSayCommand(SayCommand sayCommand) {
        return sayCommand;
    }

    @Provides
    @IntoSet
    public Command providePreferredNameCommand(PreferredNameCommand preferredNameCommand) {
        return preferredNameCommand;
    }

    @Provides
    @IntoSet
    public Command provideSearchCommand(SearchCommand searchCommand) {
        return searchCommand;
    }

    @Provides
    @IntoSet
    public Command provideFishCommand(FishCommand fishCommand) {
        return fishCommand;
    }

    @Provides
    @IntoSet
    public Command provideProfileCommand(ProfileCommand profileCommand) {
        return profileCommand;
    }
}
