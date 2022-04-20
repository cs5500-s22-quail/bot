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

    @Provides
    @IntoSet
    public SelectionMenuHandler provideShopCommandMenuHandler(ShopCommand shopCommand) {
        return shopCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideShowCommandClickHandler(ShowCommand showCommand) {
        return showCommand;
    }

    @Provides
    @IntoSet
    public Command provideShopCommand(ShopCommand shopCommand) {
        return shopCommand;
    }

    @Provides
    @IntoSet
    public Command provideShowCommand(ShowCommand showCommand) {
        return showCommand;
    }
}
