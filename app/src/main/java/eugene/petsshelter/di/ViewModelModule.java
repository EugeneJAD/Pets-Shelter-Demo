package eugene.petsshelter.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import eugene.petsshelter.viewmodel.MainViewModel;
import eugene.petsshelter.viewmodel.MapViewModel;
import eugene.petsshelter.viewmodel.PetsShelterViewModelFactory;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    abstract ViewModel bindMapViewModel(MapViewModel searchViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PetsShelterViewModelFactory factory);
}
