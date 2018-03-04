package eugene.petsshelter.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import eugene.petsshelter.ui.adoption.AdoptionInfoViewModel;
import eugene.petsshelter.ui.donation.CardDonationViewModel;
import eugene.petsshelter.ui.donation.DonationActivityViewModel;
import eugene.petsshelter.ui.donation.PaymentMethodsViewModel;
import eugene.petsshelter.ui.donation.SummaryDonationViewModel;
import eugene.petsshelter.ui.main.MainViewModel;
import eugene.petsshelter.ui.main.NewsViewModel;
import eugene.petsshelter.ui.main.PetDetailsViewModel;
import eugene.petsshelter.ui.main.PetsViewModel;
import eugene.petsshelter.ui.main.ShelterViewModel;
import eugene.petsshelter.ui.map.MapViewModel;
import eugene.petsshelter.ui.splash.SplashActivityViewModel;
import eugene.petsshelter.viewmodel.PetsShelterViewModelFactory;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    abstract ViewModel bindMapViewModel(MapViewModel mapViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PetsViewModel.class)
    abstract ViewModel bindPetsViewModel(PetsViewModel petsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PetDetailsViewModel.class)
    abstract ViewModel bindPetDetailsViewModel(PetDetailsViewModel petDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShelterViewModel.class)
    abstract ViewModel bindShelterViewModel(ShelterViewModel shelterViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DonationActivityViewModel.class)
    abstract ViewModel bindDonationActivityViewModel(DonationActivityViewModel donationActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaymentMethodsViewModel.class)
    abstract ViewModel bindPaymentMethodsViewModel(PaymentMethodsViewModel paymentMethodsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CardDonationViewModel.class)
    abstract ViewModel bindCardDonationViewModel(CardDonationViewModel cardDonationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SummaryDonationViewModel.class)
    abstract ViewModel bindSummaryDonationViewModel(SummaryDonationViewModel summaryDonationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    abstract ViewModel bindNewsViewModel(NewsViewModel newsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AdoptionInfoViewModel.class)
    abstract ViewModel bindAdoptionInfoViewModel(AdoptionInfoViewModel adoptionInfoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel.class)
    abstract ViewModel bindSplashActivityViewModel(SplashActivityViewModel splashActivityViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PetsShelterViewModelFactory factory);
}
