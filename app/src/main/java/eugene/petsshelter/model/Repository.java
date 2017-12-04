package eugene.petsshelter.model;

import android.arch.lifecycle.LiveData;

import java.util.List;

import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.model.models.Shelter;

/**
 * Created by Администратор on 03.12.2017.
 */

public interface Repository {

    LiveData<List<Pet>> getDogs();

    LiveData<List<Pet>> getCats();

    LiveData<List<Shelter>> getShelters();
}
