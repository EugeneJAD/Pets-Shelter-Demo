package eugene.petsshelter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import eugene.petsshelter.model.FirebaseRepository;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.view.ui.MainActivity;


public class PetsViewModel extends ViewModel{

    public static final String DEFAULT_FRAGMENT_TYPE = MainActivity.FRAGMENT_LIST_TYPE_DOGS;

    private FirebaseRepository firebaseRepository;
    private MutableLiveData<Pet> selectedPet = new MutableLiveData<>();;
    private String petsListFragmentType;

    public PetsViewModel(){
        firebaseRepository = FirebaseRepository.getInstance();
        petsListFragmentType = DEFAULT_FRAGMENT_TYPE;
    }

    public LiveData<List<Pet>> getDogs(){
        return firebaseRepository.getDogs();
    }

    public LiveData<List<Pet>> getCats(){
        return firebaseRepository.getCats();
    }

    public LiveData<Pet> getSelectedPet(){
        return selectedPet;
    }

    public void setSelectedPet(Pet pet){
        selectedPet.setValue(pet);
    }

    public String getPetsListFragmentType() {
        return petsListFragmentType;
    }

    public void setPetsListFragmentType(String listType) {
        petsListFragmentType = listType;
    }
}
