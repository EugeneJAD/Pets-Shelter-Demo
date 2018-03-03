package eugene.petsshelter.data.repository.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import eugene.petsshelter.data.models.AdoptionInfo;
import eugene.petsshelter.data.models.Cat;
import eugene.petsshelter.data.models.Dog;
import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.utils.AbsentLiveData;
import eugene.petsshelter.utils.AppConstants;
import timber.log.Timber;

@Singleton
public class FirebaseRepository {

    private DatabaseReference dogsDatabaseRef;
    private DatabaseReference catsDatabaseRef;
    private DatabaseReference sheltersDatabaseRef;
    private DatabaseReference favoritesDatabaseRef;
    private DatabaseReference newsDatabaseRef;
    private DatabaseReference adoptionInfoDatabaseRef;

    private ValueEventListener newsItemListener;
    private ChildEventListener newsChildEventListener;

    private MutableLiveData<List<Pet>> dogs = new MutableLiveData<>();
    private MutableLiveData<List<Pet>> cats = new MutableLiveData<>();
    private MutableLiveData<Shelter> shelter = new MutableLiveData<>();
    private MutableLiveData<HashMap<String,Boolean>> favorites = new MutableLiveData<>();
    private MutableLiveData<List<NewsItem>> news = new MutableLiveData<>();
    private MutableLiveData<NewsItem> selectedNewsItem = new MutableLiveData<>();
    private MutableLiveData<AdoptionInfo> adoptionInfo = new MutableLiveData<>();

    private List<Pet> allDogs = new ArrayList<>();
    private List<Pet> allCats = new ArrayList<>();
    private List<Shelter> allShelters = new ArrayList<>();
    private List<NewsItem> newsItems = new ArrayList<>();
    private List<String> newsItemsIndexes = new ArrayList<>();

    @Inject
    public FirebaseRepository(FirebaseDatabase database){

        database.setPersistenceEnabled(true);

        dogsDatabaseRef = database.getReference().child(AppConstants.DOGS);
        catsDatabaseRef = database.getReference().child(AppConstants.CATS);
        sheltersDatabaseRef = database.getReference().child(AppConstants.SHELTERS);
        favoritesDatabaseRef = database.getReference().child(AppConstants.FAVORITES);
        newsDatabaseRef = database.getReference().child(AppConstants.NEWS);
        adoptionInfoDatabaseRef = database.getReference().child(AppConstants.ADOPTION).child("a1");

        dogsDatabaseRef.keepSynced(true);
        catsDatabaseRef.keepSynced(true);
        sheltersDatabaseRef.keepSynced(true);
        favoritesDatabaseRef.keepSynced(true);
        newsDatabaseRef.keepSynced(true);
        adoptionInfoDatabaseRef.keepSynced(true);

        newsChildEventListener = getNewsChildEventListener();

        attachFirebaseReadListeners();
    }

    public LiveData<List<Pet>> getDogs(){return dogs;}

    public LiveData<List<Pet>> getCats(){return cats;}

    public LiveData<Shelter> getShelter() {return shelter;}

    public LiveData<List<NewsItem>> getNews() {return news;}

    public LiveData<NewsItem> getSelectedNewsItem() {return selectedNewsItem;}

    public LiveData<AdoptionInfo> getAdoptionInfo() {return adoptionInfo;}

    public void attachFirebaseReadListeners(){

        ValueEventListener dogsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!allDogs.isEmpty())
                    allDogs.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(Dog.class) != null) {
                        Dog dog = ds.getValue(Dog.class);
                        dog.setId(ds.getKey());
                        dog.setPetType(AppConstants.PET_TYPE_DOG);
                        allDogs.add(dog);
                    }
                }
                dogs.setValue(allDogs);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        dogsDatabaseRef.addListenerForSingleValueEvent(dogsListener);

        ValueEventListener catsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!allCats.isEmpty())
                    allCats.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(Cat.class) != null) {
                        Cat cat = ds.getValue(Cat.class);
                        cat.setId(ds.getKey());
                        cat.setPetType(AppConstants.PET_TYPE_CAT);
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

        ValueEventListener sheltersListener = new ValueEventListener() {
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

        ValueEventListener newsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!newsItems.isEmpty())
                    newsItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(NewsItem.class) != null) {
                        NewsItem newsItem = ds.getValue(NewsItem.class);
                        if(newsItem!=null && !TextUtils.isEmpty(newsItem.title) && !newsItem.title.equals("title")) {
                            newsItem.key = ds.getKey();
                            newsItemsIndexes.add(newsItem.key);
                            newsItems.add(newsItem);
                        }
                    }
                }
                news.setValue(newsItems);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        newsDatabaseRef.addListenerForSingleValueEvent(newsListener);

        newsItemListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewsItem item = dataSnapshot.getValue(NewsItem.class);
                if(item!=null) {
                    item.key = dataSnapshot.getKey();
                    int index = newsItemsIndexes.indexOf(item.key);
                    if (index > -1) {
                        newsItems.set(index, item);
                        news.setValue(newsItems);
                    }
                }
                selectedNewsItem.setValue(item);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        ValueEventListener adoptionInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdoptionInfo ai = dataSnapshot.getValue(AdoptionInfo.class);
                adoptionInfo.setValue(ai);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.d("onCancelled %s",databaseError);
            }
        };
        adoptionInfoDatabaseRef.addListenerForSingleValueEvent(adoptionInfoListener);
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
        if(getUser()!=null) favoritesDatabaseRef.child(getUser().getUid()).setValue(update);
    }

    public void starTransaction(String newsItemKey){

        if(getUser()==null || TextUtils.isEmpty(newsItemKey))
            return;

        newsDatabaseRef.child(newsItemKey).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                NewsItem item = mutableData.getValue(NewsItem.class);
                if (item == null) {
                    return Transaction.success(mutableData);
                }

                if (item.stars.containsKey(getUser().getUid())) {
                    // Unstar the news and remove self from stars
                    item.starCount = item.starCount - 1;
                    item.stars.remove(getUser().getUid());
                } else {
                    // Star the news and add self to stars
                    item.starCount = item.starCount + 1;
                    item.stars.put(getUser().getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(item);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Timber.d("newsTransaction:onComplete: %s", databaseError);
            }
        });
    }

    private ChildEventListener getNewsChildEventListener() {

        return new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                NewsItem newsItem = dataSnapshot.getValue(NewsItem.class);
                newsItem.key = dataSnapshot.getKey();
                int index = newsItemsIndexes.indexOf(newsItem.key);
                if(index>-1) {
                    newsItems.set(index, newsItem);
                    news.setValue(newsItems);
                }
            }
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    public void startListeningNews() {newsDatabaseRef.addChildEventListener(newsChildEventListener);}
    public void stopListeningNews() {if (newsChildEventListener != null) newsDatabaseRef.removeEventListener(newsChildEventListener);}

    public void startListeningNewsItem(String key){
        if(!TextUtils.isEmpty(key))
            newsDatabaseRef.child(key).addValueEventListener(newsItemListener);}
    public void stopListeningNewsItem(String key) {
        if(!TextUtils.isEmpty(key)) {
            if (newsItemListener != null) newsDatabaseRef.child(key).removeEventListener(newsItemListener);
        }
        selectedNewsItem.setValue(null);
    }

    private FirebaseUser getUser(){return FirebaseAuth.getInstance().getCurrentUser(); }
}
