package eugene.petsshelter.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eugene.petsshelter.view.ui.MapsActivity;


@Module
public abstract class MapActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MapsActivity contributeMapsActivity();
}
