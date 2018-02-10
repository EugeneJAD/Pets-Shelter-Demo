package eugene.petsshelter.data.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.Map;

import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Profile;
import eugene.petsshelter.data.models.Shelter;

/**
 * Created by Администратор on 03.12.2017.
 */

public interface Repository {

    LiveData<Profile> getProfile(String email);
    LiveData<List<Pet>> getDogs();
    LiveData<List<Pet>> getCats();
    LiveData<Shelter> getShelter();
    LiveData<Pet> getDogById(String id);
    LiveData<Pet> getCatById(String id);
    LiveData<String> getStripeChargeResponse();
    void createCharge(Map<String,Object> fields);
    void detachFirebaseReadListeners();
}
