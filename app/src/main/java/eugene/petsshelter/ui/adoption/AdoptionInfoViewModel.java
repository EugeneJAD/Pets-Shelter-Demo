package eugene.petsshelter.ui.adoption;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eugene.petsshelter.data.models.AdoptionInfo;
import eugene.petsshelter.data.repository.Repository;

/**
 * Created by Admin on 3/2/2018.
 */

public class AdoptionInfoViewModel extends ViewModel {

    @Inject Repository repository;

    @Inject
    public AdoptionInfoViewModel() {}

    public LiveData<AdoptionInfo> getAdoptionInfo(){return repository.getAdoptionInfo();}
}
