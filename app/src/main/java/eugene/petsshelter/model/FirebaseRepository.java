package eugene.petsshelter.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.model.models.Pet;


public class FirebaseRepository {

    private static final String TAG = FirebaseDatabase.class.getSimpleName();

    private static FirebaseRepository repository;

    public static final String DOGS = "dogs";
    public static final String CATS = "cats";

    private FirebaseDatabase mDatabase;
    private DatabaseReference dogsDatabaseRef;
    private DatabaseReference catsDatabaseRef;

    private MutableLiveData<List<Pet>> dogs;
    private MutableLiveData<List<Pet>> cats;
    private List<Pet> allDogs;
    private List<Pet> allCats;

    private ChildEventListener dogsEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    private ValueEventListener dogsListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getValue(Pet.class) != null) {
                    Pet dog = ds.getValue(Pet.class);
                    dog.setId(ds.getKey());
                    allDogs.add(dog);
                }
            }
            dogs.setValue(allDogs);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    private ValueEventListener catsListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ds.getValue(Pet.class) != null) {
                    Pet cat = ds.getValue(Pet.class);
                    cat.setId(ds.getKey());
                    allCats.add(cat);
                }
            }
            cats.setValue(allCats);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };


    private FirebaseRepository(){

        Log.i(TAG, "FirebaseRepository() Constructor");

        allDogs = new ArrayList<>();
        allCats = new ArrayList<>();
        dogs = new MutableLiveData<>();
        cats = new MutableLiveData<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);

        dogsDatabaseRef = mDatabase.getReference().child(DOGS);
        catsDatabaseRef = mDatabase.getReference().child(CATS);

        dogsDatabaseRef.addListenerForSingleValueEvent(dogsListener);
        catsDatabaseRef.addListenerForSingleValueEvent(catsListener);
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

}
