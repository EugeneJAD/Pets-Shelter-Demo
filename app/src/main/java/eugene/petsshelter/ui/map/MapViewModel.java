package eugene.petsshelter.ui.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.data.repository.Repository;


public class MapViewModel extends ViewModel{


    @Inject Repository repository;
    private LiveData<Shelter> shelter;

    @Inject
    public MapViewModel() {}

    public LiveData<Shelter> getShelter() {
        if(shelter==null) shelter = repository.getShelter();
        return shelter;
    }
}
