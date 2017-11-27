package eugene.petsshelter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import eugene.petsshelter.model.FirebaseRepository;
import eugene.petsshelter.model.models.Shelter;


public class SheltersViewModel extends ViewModel {

    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Shelter> selectedShelter;

    public SheltersViewModel(){
        selectedShelter = new MutableLiveData<>();
        firebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<List<Shelter>> getShelters(){return firebaseRepository.getShelters();}

    public LiveData<Shelter> getSelectedShelter() {
        return selectedShelter;
    }

    public void setSelectedShelter(Shelter shelter) {
        selectedShelter.setValue(shelter);
    }
}
