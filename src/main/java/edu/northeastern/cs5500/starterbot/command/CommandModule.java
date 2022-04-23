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
    public Command provideSelectCommand(SelectCommand selectCommand) {
        return selectCommand;
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
    public ButtonClickHandler provideShopCommandClickHandler(ShopCommand shopCommand) {
        return shopCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideTrainCommandClickHandler(TrainCommand trainCommand) {
        return trainCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideBattleReceivedCommandClickHandler(
            BattleReceivedCommand battleReceivedCommand) {
        return battleReceivedCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideSearchCommandClickHandler(SearchCommand searchCommand) {
        return searchCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideBattleCommandMenuHandler(BattleCommand battleCommand) {
        return battleCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideRedeemCommandMenuHandler(RedeemCommand redeemCommand) {
        return redeemCommand;
    }

    @Provides
    @IntoSet
    public SelectionMenuHandler provideGiftCommandMenuHandler(GiftCommand giftCommand) {
        return giftCommand;
    }

    @Provides
    @IntoSet
    public Command provideShopCommand(ShopCommand shopCommand) {
        return shopCommand;
    }

    @Provides
    @IntoSet
    public Command provideTrainCommand(TrainCommand trainCommand) {
        return trainCommand;
    }

    @Provides
    @IntoSet
    public Command providePokemonInfoCommand(PokemonInfoCommand pokemonInfoCommand) {
        return pokemonInfoCommand;
    }

    @Provides
    @IntoSet
    public Command provideShowCommand(ShowCommand showCommand) {
        return showCommand;
    }

    @Provides
    @IntoSet
    public Command provideBattleCommand(BattleCommand battleCommand) {
        return battleCommand;
    }

    @Provides
    @IntoSet
    public Command provideAllPokemonCommand(AllPokemonCommand allPokemonCommand) {
        return allPokemonCommand;
    }

    @Provides
    @IntoSet
    public Command provideRedeemCommand(RedeemCommand redeemCommand) {
        return redeemCommand;
    }

    @Provides
    @IntoSet
    public Command provideGiftCommand(GiftCommand giftCommand) {
        return giftCommand;
    }
}
