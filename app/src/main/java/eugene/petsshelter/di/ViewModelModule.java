package eugene.petsshelter.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import eugene.petsshelter.ui.donation.DonationActivityViewModel;
import eugene.petsshelter.ui.main.MainViewModel;
import eugene.petsshelter.ui.main.PetDetailsViewModel;
import eugene.petsshelter.ui.main.PetsViewModel;
import eugene.petsshelter.ui.main.ShelterViewModel;
import eugene.petsshelter.ui.map.MapViewModel;
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
    abstract ViewModelProvider.Factory bindViewModelFactory(PetsShelterViewModelFactory factory);
}
