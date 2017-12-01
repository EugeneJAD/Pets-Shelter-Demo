package eugene.petsshelter.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.model.models.Cat;
import eugene.petsshelter.model.models.Dog;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.model.models.Shelter;


public class FirebaseRepository {

    private static final String TAG = FirebaseDatabase.class.getSimpleName();

    private static FirebaseRepository repository;

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
    private MutableLiveData<List<Shelter>> shelters = new MutableLiveData<>();


    private List<Pet> allDogs = new ArrayList<>();
    private List<Pet> allCats = new ArrayList<>();
    private List<Shelter> allShelters = new ArrayList<>();

    private FirebaseRepository(){

        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);

        dogsDatabaseRef = mDatabase.getReference().child(DOGS);
        catsDatabaseRef = mDatabase.getReference().child(CATS);
        sheltersDatabaseRef = mDatabase.getReference().child(SHELTERS);

    }

    public synchronized static FirebaseRepository getInstance() {

        if (repository == null) {
            repository = new FirebaseRepository();
        }

        return repository;
    }


    public LiveData<List<Pet>> getDogs(){
        return dogs;
    }

    public LiveData<List<Pet>> getCats(){
        return cats;
    }

    public LiveData<List<Shelter>> getShelters() {return shelters;}

    public void attachFirebaseReadListeners(){

        if(dogsListener==null) {
            dogsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!allDogs.isEmpty())
                        allDogs.clear();
                    Log.i(TAG, "dogsListener activated");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(Dog.class) != null) {
                            Dog dog = ds.getValue(Dog.class);
                            dog.setId(ds.getKey());
                            Log.i(TAG, "ds.getKey() "+ds.getKey() +" food = " + dog.getFoodCount());
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

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getValue(Shelter.class) != null) {
                            Shelter shelter = ds.getValue(Shelter.class);
                            shelter.setId(ds.getKey());
                            allShelters.add(shelter);
                        }
                    }
                    shelters.setValue(allShelters);
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

                Log.i(TAG, "postTransaction:onComplete:" + databaseError);
                Log.i(TAG, "transaction successfully completed = " + b);
            }
        });

    }

}
