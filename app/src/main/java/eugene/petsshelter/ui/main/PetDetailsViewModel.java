package eugene.petsshelter.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import javax.inject.Inject;

import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.utils.AbsentLiveData;
import eugene.petsshelter.utils.AppConstants;

/**
 * Created by Eugene on 01.02.2018.
 */

public class PetDetailsViewModel extends ViewModel {

    @Inject Repository repository;

    private LiveData<Pet> pet;
    private MutableLiveData<PetId> petId = new MutableLiveData<>();

    @Inject
    public PetDetailsViewModel() {

        pet = Transformations.switchMap(petId, values -> {

            if (TextUtils.isEmpty(values.id))
                return AbsentLiveData.create();
            else if(values.type.equals(AppConstants.PET_TYPE_CAT))
                return repository.getCatById(values.id);
            else
                return repository.getDogById(values.id);
        });
    }

    public void setPetId(String id, String type) {
        if (petId.getValue() != null && petId.getValue().id.equals(id))
            return;
        PetId update = new PetId(id, type);
        petId.setValue(update);
    }

    public LiveData<Pet> getPet() {return pet;}

    public void addOrRemoveFavorite(String id) {repository.addToFavorites(id);}

    static class PetId {
        public final String id;
        public final String type;

        public PetId(String _id, String type) {
            this.id = _id;
            this.type = type;
        }
    }
}
