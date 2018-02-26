package eugene.petsshelter.data.repository;

import android.arch.lifecycle.LiveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eugene.petsshelter.data.models.ApiResponse;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Profile;
import eugene.petsshelter.data.models.Shelter;
import okhttp3.ResponseBody;

/**
 * Created by Администратор on 03.12.2017.
 */

public interface Repository {

    LiveData<Profile> getProfile();
    LiveData<List<Pet>> getDogs();
    LiveData<List<Pet>> getCats();
    LiveData<Shelter> getShelter();
    LiveData<Pet> getDogById(String id);
    LiveData<Pet> getCatById(String id);
    LiveData<HashMap<String,Boolean>> getFavorites();
    LiveData<ApiResponse<ResponseBody>> createCharge(Map<String,Object> fields);
    void addToFavorites(String petId);
    boolean isFavorite(String petId);
    void updateRemoteFavoritePets();
    void updateLocalFavoritePets(HashMap<String, Boolean> favorites);
}
