package eugene.petsshelter.viewmodel;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eugene.petsshelter.model.FirebaseRepository;
import eugene.petsshelter.model.models.Shelter;


public class MapViewModel extends ViewModel{


    private FirebaseRepository firebaseRepository;
    private Shelter selectedShelter;

    @Inject
    public MapViewModel(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
     }

    public Shelter getSelectedShelter(String _id) {

        if (selectedShelter == null && firebaseRepository.getShelters().getValue()!=null) {
           for (Shelter shelter: firebaseRepository.getShelters().getValue())
               if(shelter.getId().equals(_id))
                   selectedShelter=shelter;
        }
        return selectedShelter;
    }
}
