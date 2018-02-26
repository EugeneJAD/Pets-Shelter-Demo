package eugene.petsshelter.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import java.util.HashMap;

import javax.inject.Inject;

import eugene.petsshelter.data.models.Profile;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.utils.AbsentLiveData;


public class MainViewModel extends ViewModel{

    @Inject Repository repository;

    private LiveData<Profile> profile;
    private LiveData<HashMap<String, Boolean>> favorites;
    private MutableLiveData<String> userId = new MutableLiveData<>();

    @Inject
    public MainViewModel(){

        profile = Transformations.switchMap(userId, id -> {
            if (TextUtils.isEmpty(id)) {
                return AbsentLiveData.create();
            } else {
                return repository.getProfile();
            }
        });

        favorites = Transformations.switchMap(profile, profile -> {
            if (profile==null || TextUtils.isEmpty(profile.getUId())) {
                repository.updateLocalFavoritePets(null);
                return AbsentLiveData.create();
            } else
                return repository.getFavorites();
        });

    }

    @Override
    protected void onCleared() {
        repository.updateRemoteFavoritePets();
        super.onCleared();
    }

    public LiveData<Profile> getProfile() {return profile;}

    public LiveData<HashMap<String, Boolean>> getFavorites() {return favorites;}

    public void reloadUserData(String id){userId.setValue(id);}

}
