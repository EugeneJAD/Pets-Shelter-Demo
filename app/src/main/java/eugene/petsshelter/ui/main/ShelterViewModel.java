package eugene.petsshelter.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.data.repository.Repository;

/**
 * Created by Eugene on 01.02.2018.
 */

public class ShelterViewModel extends ViewModel {

    @Inject Repository repository;

    private LiveData<Shelter> shelter;

    @Inject
    public ShelterViewModel() {}

    public LiveData<Shelter> getShelter() {
        if(shelter==null) shelter=repository.getShelter();
        return shelter;
    }
}
