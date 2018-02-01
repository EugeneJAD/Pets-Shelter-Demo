package eugene.petsshelter.di;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.ui.map.MapsActivity;


@Module
public class MapActivityModule {

    @Provides
    FragmentActivity provideActivity(MapsActivity activity) {return activity;}
}