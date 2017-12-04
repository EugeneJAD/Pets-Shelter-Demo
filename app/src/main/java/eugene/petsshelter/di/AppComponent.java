package eugene.petsshelter.di;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import eugene.petsshelter.PetsShelterApp;

@Component(modules = {AppModule.class,MainActivityModule.class, MapActivityModule.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(PetsShelterApp petsShelterApp);
}
