package eugene.petsshelter.data.repository.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import eugene.petsshelter.data.models.Cat;
import eugene.petsshelter.data.models.Dog;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Shelter;
import timber.log.Timber;

@Singleton
public class FirebaseRepository {

    public static final String DOGS = "dogs";
    public static final String CATS = "cats";
    public static final String SHELTERS = "shelters";
    public static final String NEWS = "news";
    public static final String FAVORITES = "favorites";

    private DatabaseReference dogsDatabaseRef;
    private DatabaseReference catsDatabaseRef;
    private DatabaseReference sheltersDatabaseRef;
    private DatabaseReference favoritesDatabaseRef;

    private ValueEventListener dogsListener;
    private ValueEventListener catsListener;
    private ValueEventListener sheltersListener;

    private MutableLiveData<List<Pet>> dogs = new MutableLiveData<>();
    private MutableLiveData<List<Pet>> cats = new MutableLiveData<>();
    private MutableLiveData<Shelter> shelter = new MutableLiveData<>();
    private MutableLiveData<HashMap<String,Boolean>> favorites = new MutableLiveData<>();

    private List<Pet> allDogs = new ArrayList<>();
    private List<Pet> allCats = new ArrayList<>();
    private List<Shelter> allShelters = new ArrayList<>();

    @Inject
    public FirebaseRepository(FirebaseDatabase database){

        database.setPersistenceEnabled(true);

        dogsDatabaseRef = database.getReference().child(DOGS);
        catsDatabaseRef = database.getReference().child(CATS);
        sheltersDatabaseRef = database.getReference().child(SHELTERS);
        favoritesDatabaseRef = database.getReference().child(FAVORITES);

        dogsDatabaseRef.keepSynced(true);
        catsDatabaseRef.keepSynced(true);
        sheltersDatabaseRef.keepSynced(true);
        favoritesDatabaseRef.keepSynced(true);

        attachFirebaseReadListeners();
    }

    public LiveData<List<Pet>> getDogs(){return dogs;}

    public LiveData<List<Pet>> getCats(){return cats;}

    public LiveData<Shelter> getShelter() {return shelter;}

    public void attachFirebaseReadListeners(){

        if(dogsListener==null) {
            dogsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!allDogs.isEmpty())
                        allDogs.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(Dog.class) != null) {
                            Dog dog = ds.getValue(Dog.class);
                            dog.setId(ds.getKey());
                            allDogs.add(dog);
                        }
                    }
                    dogs.setValue(allDogs);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            dogsDatabaseRef.addListenerForSingleValueEvent(dogsListener);
        }

        if(catsListener==null) {
            catsListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!allCats.isEmpty())
                        allCats.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(Cat.class) != null) {
                            Cat cat = ds.getValue(Cat.class);
                            cat.setId(ds.getKey());
                            allCats.add(cat);
                        }
                    }
                    cats.setValue(allCats);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            catsDatabaseRef.addListenerForSingleValueEvent(catsListener);
        }

        if(sheltersListener==null) {
            sheltersListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!allShelters.isEmpty())
                        allShelters.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(Shelter.class) != null) {
                            Shelter shelter = ds.getValue(Shelter.class);
                            shelter.setId(ds.getKey());
                            allShelters.add(shelter);
                        }
                    }
                    if(!allShelters.isEmpty()) shelter.setValue(allShelters.get(0));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            sheltersDatabaseRef.addListenerForSingleValueEvent(sheltersListener);
        }
    }

    public LiveData<HashMap<String,Boolean>> getUsersFavPets(HashMap<String,Boolean> localFavPets) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            favoritesDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            localFavPets.put(ds.getKey(), true);
                        favorites.setValue(localFavPets);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        return favorites;
    }

    public void syncFavorites(HashMap<String, Boolean> update) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) favoritesDatabaseRef.child(user.getUid()).setValue(update);
    }
}
