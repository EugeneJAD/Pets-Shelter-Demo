package eugene.petsshelter.di;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.data.repository.DataRepository;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.data.repository.remote.FirebaseRepository;
import eugene.petsshelter.ui.main.MainViewModel;
import eugene.petsshelter.ui.main.PetDetailsViewModel;
import eugene.petsshelter.ui.main.PetsViewModel;
import eugene.petsshelter.ui.main.ShelterViewModel;
import eugene.petsshelter.ui.map.MapViewModel;


@Module (includes = ViewModelModule.class)
class AppModule {

    @Provides
    FirebaseDatabase provideFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

    @Provides @Singleton
    FirebaseRepository provideFirebaseRepositoty(FirebaseDatabase db){
        return new FirebaseRepository(db);
    }

    @Provides @Singleton
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
}
