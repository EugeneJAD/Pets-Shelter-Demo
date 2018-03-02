package eugene.petsshelter.di;

import android.app.Application;
import android.content.res.Resources;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.data.repository.DataRepository;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.data.repository.local.PrefsManager;
import eugene.petsshelter.data.repository.remote.FirebaseRepository;
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


@Module (includes = ViewModelModule.class)
class AppModule {


    @Provides
    @Singleton
    PrefsManager providePrefsManager(Application application) {return new PrefsManager(application);}

    @Provides
    @Singleton
    Resources provideResources(Application application) {
        return application.getResources();
    }

    @Provides
    FirebaseDatabase provideFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    FirebaseRepository provideFirebaseRepositoty(FirebaseDatabase db){
        return new FirebaseRepository(db);
    }

    @Provides
    @Singleton
    Repository provideRepository(DataRepository dataRepository){return dataRepository;}

    @Provides
    Class<MainViewModel> provideMainViewModel(){return MainViewModel.class;}

    @Provides
    Class<MapViewModel> provideMapViewModel(){return MapViewModel.class;}

    @Provides
    Class<PetsViewModel> providePetsViewModel(){return PetsViewModel.class;}

    @Provides
    Class<PetDetailsViewModel> providePetDetailsViewModel(){return PetDetailsViewModel.class;}

    @Provides
    Class<ShelterViewModel> provideShelterViewModel(){return ShelterViewModel.class;}

    @Provides
    Class<DonationActivityViewModel> provideDonationActivityViewModel(){return DonationActivityViewModel.class;}

    @Provides
    Class<PaymentMethodsViewModel> providePaymentMethodsViewModel(){return PaymentMethodsViewModel.class;}

    @Provides
    Class<CardDonationViewModel> provideCardDonationViewModel(){return CardDonationViewModel.class;}

    @Provides
    Class<SummaryDonationViewModel> provideSummaryDonationViewModel(){return SummaryDonationViewModel.class;}

    @Provides
    Class<NewsViewModel> provideNewsViewModel(){return NewsViewModel.class;}
}
