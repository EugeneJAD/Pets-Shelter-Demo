package eugene.petsshelter.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eugene.petsshelter.ui.main.PetDetailsFragment;
import eugene.petsshelter.ui.main.PetsListFragment;
import eugene.petsshelter.ui.main.ShelterDetailsFragment;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract PetsListFragment contributePetsListFragment();

    @ContributesAndroidInjector
    abstract PetDetailsFragment contributeDetailsFragment();

    @ContributesAndroidInjector
    abstract ShelterDetailsFragment contributeShelterDetailsFragment();
}