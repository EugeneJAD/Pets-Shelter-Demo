package eugene.petsshelter.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Profile;
import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.data.repository.remote.FirebaseRepository;
import eugene.petsshelter.service.StripeService;
import eugene.petsshelter.ui.main.PetsListFragment;
import eugene.petsshelter.utils.AbsentLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Eugene on 31.01.2018.
 */
@Singleton
public class DataRepository implements Repository {

    public static final String PAYMENT_DONE = "Payment done!";
    public static final String PAYMENT_FAILED = "Payment failed.";


    @Inject FirebaseRepository remoteRepo;
    @Inject StripeService stripeService;

    private MutableLiveData<Profile> profile = new MutableLiveData<>();

    //notify user
    private MutableLiveData<String> apiResponse = new MutableLiveData<>();

    @Inject
    public DataRepository() {}

    @Override
    public LiveData<Profile> getProfile(String email) {
        if (profile.getValue() != null && profile.getValue().getEmail().equals(email)) {return profile;}
        else {return getUserProfile();}
    }

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
    public LiveData<String> getStripeChargeResponse() {return apiResponse;}

    @Override
    public void createCharge(Map<String,Object> fields) {

        stripeService.chargeDonation(fields).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){notifyUser(PAYMENT_DONE);}
                else {notifyUser(PAYMENT_FAILED);}
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {notifyUser(PAYMENT_FAILED);}
        });
    }

    private void notifyUser(String message) {
        apiResponse.setValue(message);
        apiResponse.setValue(null);}


    @Override
    public void detachFirebaseReadListeners() {
        Timber.d("detachFirebaseReadListeners");
        remoteRepo.detachFirebaseReadListeners();
    }

    private LiveData<Profile> getUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){return AbsentLiveData.create();}

        Profile p = new Profile(user.getDisplayName(),user.getPhoneNumber(),user.getEmail(),user.getPhotoUrl().toString());
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
