package eugene.petsshelter.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.utils.AbsentLiveData;

/**
 * Created by Eugene on 31.01.2018.
 */

public class PetsViewModel extends ViewModel {

    @Inject Repository repository;

    private MutableLiveData<String> listType = new MutableLiveData<>();
    private LiveData<List<Pet>> pets;

    @Inject
    public PetsViewModel() {

        pets = Transformations.switchMap(listType,type ->{
            if (TextUtils.isEmpty(type))
                return AbsentLiveData.create();
            else if(type.equals(PetsListFragment.FRAGMENT_LIST_TYPE_DOGS))
                return repository.getDogs();
            else
                return repository.getCats();
        });
    }

    public void setListType(String type) {
        if(listType.getValue()!=null && listType.getValue().equals(type))
            return;
        listType.setValue(type);
    }

    public LiveData<String> getListType() {return listType;}

    public LiveData<List<Pet>> getPets() {return pets;}

    public void addOrRemoveFavorite(String id) {repository.addToFavorites(id);}

    public LiveData<HashMap<String, Boolean>> getFavorites() {return repository.getFavorites();}
}
