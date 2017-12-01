package eugene.petsshelter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import eugene.petsshelter.model.FirebaseRepository;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.model.models.Shelter;
import eugene.petsshelter.view.ui.MainActivity;


public class MainViewModel extends ViewModel{

    private static final String TAG = MainViewModel.class.getSimpleName();

    public static final String DEFAULT_FRAGMENT_TYPE = MainActivity.FRAGMENT_LIST_TYPE_DOGS;

    private FirebaseRepository firebaseRepository;

    private MutableLiveData<Pet> selectedPet = new MutableLiveData<>();
    private MutableLiveData<Shelter> selectedShelter = new MutableLiveData<>();

    private String petsListFragmentType;


    public MainViewModel(){
        firebaseRepository = FirebaseRepository.getInstance();
        firebaseRepository.attachFirebaseReadListeners();
        petsListFragmentType = DEFAULT_FRAGMENT_TYPE;
    }

    //Pets
    public LiveData<List<Pet>> getDogs(){

        return firebaseRepository.getDogs();
    }

    public LiveData<List<Pet>> getCats(){
        return firebaseRepository.getCats();
    }

    public LiveData<Pet> getSelectedPet(){return selectedPet;}

    public void setSelectedPet(Pet pet){selectedPet.setValue(pet);}

    public String getPetsListFragmentType() {
        return petsListFragmentType;
    }

    public void setPetsListFragmentType(String listType) {
        petsListFragmentType = listType;
    }

    public void buyFood(){
        if(getSelectedPet()!=null)
            firebaseRepository.incrementPetFoodCount(selectedPet.getValue());
    }

    //Shelters
    public LiveData<List<Shelter>> getShelters(){return firebaseRepository.getShelters();}

    public LiveData<Shelter> getSelectedShelter() {return selectedShelter;}


    public void setSelectedShelter(Shelter shelter) {
        selectedShelter.setValue(shelter);
    }



    @Override
    protected void onCleared() {

        Log.i(TAG, "onCleared()");
        firebaseRepository.detachFirebaseReadListeners();
        super.onCleared();
    }
}
