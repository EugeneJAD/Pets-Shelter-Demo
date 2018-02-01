package eugene.petsshelter.di;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.ui.main.MainActivity;

@Module
public class MainActivityModule {

    @Provides
    FragmentActivity provideActivity(MainActivity activity) {return activity;}
}