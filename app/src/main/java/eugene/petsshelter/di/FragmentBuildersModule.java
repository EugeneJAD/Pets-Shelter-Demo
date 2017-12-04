package eugene.petsshelter.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eugene.petsshelter.view.ui.PetDetailsFragment;
import eugene.petsshelter.view.ui.PetsListFragment;
import eugene.petsshelter.view.ui.ShelterDetailsFragment;
import eugene.petsshelter.view.ui.SheltersListFragment;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract PetsListFragment contributePetsListFragment();

    @ContributesAndroidInjector
    abstract SheltersListFragment contributeSheltersListFragment();

    @ContributesAndroidInjector
    abstract PetDetailsFragment contributeDetailsFragment();

    @ContributesAndroidInjector
    abstract ShelterDetailsFragment contributeShelterDetailsFragment();
}