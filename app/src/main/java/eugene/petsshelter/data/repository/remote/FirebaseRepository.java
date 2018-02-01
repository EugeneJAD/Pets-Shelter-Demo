package eugene.petsshelter.data.repository.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import eugene.petsshelter.data.models.Cat;
import eugene.petsshelter.data.models.Dog;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Shelter;

@Singleton
public class FirebaseRepository {

    public static final String DOGS = "dogs";
    public static final String CATS = "cats";
    public static final String SHELTERS = "shelters";

    private FirebaseDatabase mDatabase;
    private DatabaseReference dogsDatabaseRef;
    private DatabaseReference catsDatabaseRef;
    private DatabaseReference sheltersDatabaseRef;

    private ValueEventListener dogsListener;
    private ValueEventListener catsListener;
    private ValueEventListener sheltersListener;

    private MutableLiveData<List<Pet>> dogs = new MutableLiveData<>();
    private MutableLiveData<List<Pet>> cats = new MutableLiveData<>();
    private MutableLiveData<Shelter> shelter = new MutableLiveData<>();


    private List<Pet> allDogs = new ArrayList<>();
    private List<Pet> allCats = new ArrayList<>();
    private List<Shelter> allShelters = new ArrayList<>();

    @Inject
    public FirebaseRepository(FirebaseDatabase database){

        mDatabase = database;
        mDatabase.setPersistenceEnabled(true);

        dogsDatabaseRef = mDatabase.getReference().child(DOGS);
        catsDatabaseRef = mDatabase.getReference().child(CATS);
        sheltersDatabaseRef = mDatabase.getReference().child(SHELTERS);

        dogsDatabaseRef.keepSynced(true);
        catsDatabaseRef.keepSynced(true);
        sheltersDatabaseRef.keepSynced(true);

        attachFirebaseReadListeners();
    }

    public LiveData<List<Pet>> getDogs(){
        return dogs;
    }

    public LiveData<List<Pet>> getCats(){
        return cats;
    }

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
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            dogsDatabaseRef.addValueEventListener(dogsListener);
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
            catsDatabaseRef.addValueEventListener(catsListener);
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
                    if(!allShelters.isEmpty())
                        shelter.setValue(allShelters.get(0));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            sheltersDatabaseRef.addValueEventListener(sheltersListener);
        }
    }

    public void detachFirebaseReadListeners(){

        if(dogsListener!=null)
            dogsDatabaseRef.removeEventListener(dogsListener);
        if(catsListener!=null)
            catsDatabaseRef.removeEventListener(catsListener);
        if(sheltersListener!=null)
            sheltersDatabaseRef.removeEventListener(sheltersListener);

    }

    public void incrementPetFoodCount(Pet pet){

        DatabaseReference foodCountRef;
        if(pet instanceof Dog)
            foodCountRef = dogsDatabaseRef.child(pet.getId()).child("foodCount");
        else
            foodCountRef = catsDatabaseRef.child(pet.getId()).child("foodCount");

        foodCountRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                if(mutableData.getValue(Integer.class)==null)
                    return Transaction.success(mutableData);

                int foodCount = mutableData.getValue(Integer.class).intValue();
                mutableData.setValue(++foodCount);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

    }

}
