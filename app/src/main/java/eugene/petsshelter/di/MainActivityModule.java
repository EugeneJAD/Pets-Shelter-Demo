package eugene.petsshelter.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eugene.petsshelter.view.ui.MainActivity;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();
}