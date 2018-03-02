package eugene.petsshelter.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import eugene.petsshelter.data.models.ApiResponse;
import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Profile;
import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.data.repository.remote.FirebaseRepository;
import eugene.petsshelter.service.StripeService;
import eugene.petsshelter.ui.main.PetsListFragment;
import eugene.petsshelter.utils.AbsentLiveData;
import eugene.petsshelter.utils.Objects;
import okhttp3.ResponseBody;

/**
 * Created by Eugene on 31.01.2018.
 */
@Singleton
public class DataRepository implements Repository {

    @Inject FirebaseRepository remoteRepo;
    @Inject StripeService stripeService;

    private MutableLiveData<Profile> profile = new MutableLiveData<>();
    private HashMap<String,Boolean> localFavPets = new HashMap<>();

    @Inject
    public DataRepository() {}

    @Override
    public LiveData<Profile> getProfile() {return getUserProfile();}

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
    public LiveData<NewsItem> getSelectedNews() {return remoteRepo.getSelectedNewsItem();}

    @Override
    public LiveData<ApiResponse<ResponseBody>> createCharge(Map<String, Object> fields) {
        return stripeService.chargeDonation(fields);
    }

    @Override
    public LiveData<HashMap<String, Boolean>> getFavorites() {return remoteRepo.getUsersFavPets(localFavPets);}

    @Override
    public LiveData<List<NewsItem>> getNews() {return remoteRepo.getNews();}

    @Override
    public void startListeningNews() {remoteRepo.startListeningNews();}

    @Override
    public void stopListeningNews() {remoteRepo.stopListeningNews();}

    @Override
    public void startListeningNewsItem(String key) {remoteRepo.startListeningNewsItem(key);}

    @Override
    public void stopListeningNewsItem(String key) {remoteRepo.stopListeningNewsItem(key);}

    @Override
    public void addToFavorites(String petId){
        if(localFavPets.get(petId)==null) {localFavPets.put(petId,true);
        } else {localFavPets.remove(petId);}
    }

    @Override
    public boolean isFavorite(String petId) {
        return !(petId == null || localFavPets.isEmpty()) && localFavPets.containsKey(petId);
    }

    @Override
    public void updateLocalFavoritePets(HashMap<String, Boolean> update) {
        if(update==null) {
            localFavPets.clear();
        } else {
            for (String key : update.keySet())
                localFavPets.put(key, true);
        }
    }


    @Override
    public void updateRemoteFavoritePets() {remoteRepo.syncFavorites(localFavPets);}

    @Override
    public void doStarTransaction(String newsItemKey) {remoteRepo.starTransaction(newsItemKey);}

    private LiveData<Profile> getUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){return AbsentLiveData.create();}

        String photoUrl = null;
        if(user.getPhotoUrl()!=null)
            photoUrl = user.getPhotoUrl().toString();

        Profile p = new Profile(user.getUid(),user.getDisplayName(),user.getPhoneNumber(),user.getEmail(),photoUrl);
        profile.setValue(p);

        return profile;
    }

    private LiveData<Pet> findPetById(String id, String type){

        MutableLiveData<Pet> pet = new MutableLiveData<>();

        List<Pet> pets;
        if(type.equals(PetsListFragment.FRAGMENT_LIST_TYPE_DOGS)) pets = remoteRepo.getDogs().getValue();
        else pets = remoteRepo.getCats().getValue();

        if(pets==null || pets.isEmpty()) {return AbsentLiveData.create();}

        for(Pet p: pets) {
            if (p.getId().equals(id)) {
                pet.setValue(p);
                return pet;
            }
        }

        return AbsentLiveData.create();
    }






}
