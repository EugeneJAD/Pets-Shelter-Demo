package eugene.petsshelter.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.data.repository.remote.FirebaseRepository;
import eugene.petsshelter.ui.main.PetsListFragment;
import eugene.petsshelter.utils.AbsentLiveData;
import timber.log.Timber;

/**
 * Created by Eugene on 31.01.2018.
 */
@Singleton
public class DataRepository implements Repository {

    @Inject FirebaseRepository remoteRepo;

    @Inject
    public DataRepository() {}

    @Override
    public LiveData<List<Pet>> getDogs() {return remoteRepo.getDogs();}

    @Override
    public LiveData<List<Pet>> getCats() {
        return remoteRepo.getCats();
    }

    @Override
    public LiveData<Shelter> getShelter() {return remoteRepo.getShelter();}

    @Override
    public LiveData<Pet> getDogById(String id) {return findPetById(id, PetsListFragment.FRAGMENT_LIST_TYPE_DOGS);}

    @Override
    public LiveData<Pet> getCatById(String id) {return findPetById(id, PetsListFragment.FRAGMENT_LIST_TYPE_CATS);}


    @Override
    public void detachFirebaseReadListeners() {
        Timber.d("detachFirebaseReadListeners");
        remoteRepo.detachFirebaseReadListeners();
    }

    private LiveData<Pet> findPetById(String id, String type){

        MutableLiveData<Pet> pet = new MutableLiveData<>();

        List<Pet> pets;
        if(type.equals(PetsListFragment.FRAGMENT_LIST_TYPE_DOGS)) pets = remoteRepo.getDogs().getValue();
        else pets = remoteRepo.getCats().getValue();

        if(pets==null || pets.isEmpty()) {return AbsentLiveData.create();}

        for(Pet p: pets) {
            if (p.getId().equals(id)) pet.setValue(p);
        }

        if(pet.getValue()==null){return AbsentLiveData.create();}
        else return pet;
    }

}
