package eugene.petsshelter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import eugene.petsshelter.model.FirebaseRepository;
import eugene.petsshelter.model.models.Pet;


public class PetsViewModel extends ViewModel{

    private FirebaseRepository firebaseRepository;
    private final MutableLiveData<Pet> selectedPet;

    public PetsViewModel(){
        selectedPet = new MutableLiveData<>();
        firebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<List<Pet>> getDogs(){
        return firebaseRepository.getDogs();
    }

    public LiveData<Pet> getSelectedPet(){
        return selectedPet;
    }

    public void setSelectedPet(Pet pet){
        selectedPet.setValue(pet);
    }

}
