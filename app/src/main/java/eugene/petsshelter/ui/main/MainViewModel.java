package eugene.petsshelter.ui.main;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eugene.petsshelter.data.repository.Repository;


public class MainViewModel extends ViewModel{

    @Inject Repository repository;

    @Inject
    public MainViewModel(){}

    @Override
    protected void onCleared() {
        repository.detachFirebaseReadListeners();
        super.onCleared();
    }
}
