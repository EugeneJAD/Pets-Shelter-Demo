package eugene.petsshelter.di;

import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.model.FirebaseRepository;


@Module (includes = ViewModelModule.class)
class AppModule {

    @Provides
    FirebaseDatabase provideFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

    @Singleton @Provides
    FirebaseRepository provideFirebaseRepositoty(FirebaseDatabase db){
        return new FirebaseRepository(db);
    }

}
