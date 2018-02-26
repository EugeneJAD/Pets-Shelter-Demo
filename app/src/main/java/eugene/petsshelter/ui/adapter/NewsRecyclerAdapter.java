package eugene.petsshelter.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.data.repository.remote.FirebaseRepository;
import eugene.petsshelter.databinding.NewsListItemBinding;
import eugene.petsshelter.ui.base.DataBoundListAdapter;
import eugene.petsshelter.utils.Objects;
import timber.log.Timber;

/**
 * Created by Eugene on 2/25/2018.
 */

public class NewsRecyclerAdapter extends DataBoundListAdapter<NewsItem, NewsListItemBinding> {

    private OnItemClickListener<NewsItem> clickCallback;
    private DatabaseReference newsRef;
    private List<NewsItem> newsItems = new ArrayList<>();
    private List<String> newsItemsIndexes = new ArrayList<>();
    private ChildEventListener childEventListener;

    public NewsRecyclerAdapter(OnItemClickListener<NewsItem> clickCallback) {
        Timber.d("Constructor");
        this.clickCallback = clickCallback;
        childEventListener = defineChildEventListener();
        newsRef = FirebaseDatabase.getInstance().getReference().child(FirebaseRepository.NEWS);
        setItems(newsItems);
        startListening();
    }


    @Override
    protected NewsListItemBinding createBinding(ViewGroup parent) {
        NewsListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.news_list_item,parent,false);

        binding.getRoot().setOnClickListener(v -> clickCallback.onItemClick(binding.getNewsItem(),v));

        binding.newsStarButton.setOnClickListener(v1->{
            String itemKey = binding.getNewsItem().key;
            if(getUser()!=null && !TextUtils.isEmpty(itemKey)) onStarClicked(itemKey);});

        return binding;
    }


    @Override
    protected void bind(NewsListItemBinding binding, NewsItem item) {

        if(getUser()!=null && !TextUtils.isEmpty(item.key)){
            if(item.stars.containsKey(getUser().getUid())) item.isFavorite = true;
            else item.isFavorite = false;
        } else {item.isFavorite = false;}

        binding.setNewsItem(item);
        binding.setIsUserLoggedIn(getUser()!=null);
    }

    @Override
    protected boolean areItemsTheSame(NewsItem oldItem, NewsItem newItem) {return Objects.equals(oldItem.key,newItem.key);}

    @Override
    protected boolean areContentsTheSame(NewsItem oldItem, NewsItem newItem) {return Objects.equals(oldItem.starCount,newItem.starCount);}

    private void onStarClicked(String itemKey) {

        newsRef.child(itemKey).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                NewsItem item = mutableData.getValue(NewsItem.class);
                if (item == null) {
                    return Transaction.success(mutableData);
                }

                if (item.stars.containsKey(getUser().getUid())) {
                    // Unstar the post and remove self from stars
                    item.starCount = item.starCount - 1;
                    item.stars.remove(getUser().getUid());
                } else {
                    // Star the post and add self to stars
                    item.starCount = item.starCount + 1;
                    item.stars.put(getUser().getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(item);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Timber.d("postTransaction:onComplete: %s", databaseError);
            }
        });
    }

    private ChildEventListener defineChildEventListener() {

        return new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Timber.d("onChildAdded");
                NewsItem newsItem = dataSnapshot.getValue(NewsItem.class);
                if(newsItem!=null && !TextUtils.isEmpty(newsItem.title) && !newsItem.title.equals("title")) {
                    newsItem.key = dataSnapshot.getKey();
                    newsItemsIndexes.add(newsItem.key);
                    newsItems.add(newsItem);
                }
                notifyItemInserted(newsItemsIndexes.indexOf(newsItem.key)-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                NewsItem newsItem = dataSnapshot.getValue(NewsItem.class);
                newsItem.key = dataSnapshot.getKey();
                int index = newsItemsIndexes.indexOf(newsItem.key);
                if(index>-1) {
                    newsItems.set(index, newsItem);
                    replace(newsItems);
                }
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    private FirebaseUser getUser(){return FirebaseAuth.getInstance().getCurrentUser(); }

    public void startListening() {
        Timber.d("startListening");
        newsRef.addChildEventListener(childEventListener);}

    public void stopListening() {
        Timber.d("stopListening");
        if (childEventListener != null) newsRef.removeEventListener(childEventListener);
    }

}
