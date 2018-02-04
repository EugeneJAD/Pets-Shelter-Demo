package eugene.petsshelter.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import javax.inject.Inject;

import eugene.petsshelter.data.models.Profile;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.utils.AbsentLiveData;


public class MainViewModel extends ViewModel{

    @Inject Repository repository;

    private LiveData<Profile> profile;
    private MutableLiveData<String> email = new MutableLiveData<>();

    @Inject
    public MainViewModel(){

        profile = Transformations.switchMap(email, email ->{
           if(TextUtils.isEmpty(email))
               return AbsentLiveData.create();
           else
               return repository.getProfile(email);
        });
    }

    @Override
    protected void onCleared() {
        repository.detachFirebaseReadListeners();
        super.onCleared();
    }

    public LiveData<Profile> getProfile() {return profile;}

    public void reloadUserData(String email){this.email.setValue(email);}

}
