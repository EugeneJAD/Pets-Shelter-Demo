package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Comment;
import eugene.petsshelter.databinding.FragmentNewsDetailsBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.CommentsRecyclerAdapter;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.AppConstants;
import eugene.petsshelter.utils.DateUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends BaseFragment<FragmentNewsDetailsBinding, NewsViewModel>
        implements Injectable, FirebaseAuth.AuthStateListener {

    private DatabaseReference commentsDatabaseReference;
    private String selectedNewsItemId;
    private String toolbarImageUrl;
    private String toolbarTitle;

    private CommentsRecyclerAdapter commentsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_news_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null) {
            selectedNewsItemId = getArguments().getString(AppConstants.NEWS_ITEM_ID_KEY,null);
            toolbarImageUrl = getArguments().getString(AppConstants.TOOLBAR_IMAGE_URL_KEY, null);
            toolbarTitle = getArguments().getString(AppConstants.TOOLBAR_TITLE,null);
        }

        ((MainActivity) getActivity()).setActivityView(null, toolbarImageUrl, MainActivity.TYPE_FRAGMENT_NEWS_DETAILS);

        binding.newsDStarButton.setOnClickListener(v -> {
            if(binding.getNewsItem()!=null) {viewModel.doStarTransaction(binding.getNewsItem().key);}
        });

        binding.setIsUserLoggedIn(getUser()!=null);

        showHideCommentButton();

        if(!TextUtils.isEmpty(selectedNewsItemId)) {

            commentsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("news-comments").child(selectedNewsItemId);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                    true);
            layoutManager.setStackFromEnd(true);
            binding.commentsList.setLayoutManager(layoutManager);
            commentsAdapter = new CommentsRecyclerAdapter(commentsDatabaseReference);
            binding.commentsList.setAdapter(commentsAdapter);
        }

        binding.postCommentButton.setOnClickListener(v -> postComment());

        observeViewModel();
    }


    private void observeViewModel() {

        viewModel.getSelectedNews().observe(this, newsItem -> {
            if(newsItem!=null){
                if(getUser()!=null){
                    if(newsItem.stars.containsKey(getUser().getUid())) newsItem.isFavorite = true;
                    else newsItem.isFavorite = false;
                } else {newsItem.isFavorite = false;}

                binding.setNewsItem(newsItem);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
        viewModel.startListeningNewsItem(selectedNewsItemId);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        viewModel.stopListeningNewsItem(selectedNewsItemId);
        if(commentsAdapter!=null) commentsAdapter.cleanupListener();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        binding.setIsUserLoggedIn(firebaseAuth.getCurrentUser()!=null);
        showHideCommentButton();
    }

    private FirebaseUser getUser(){return FirebaseAuth.getInstance().getCurrentUser(); }

    private void postComment() {

        String text = binding.commentDBodyEdit.getText().toString().trim();
        FirebaseUser user = getUser();

        if(user==null || TextUtils.isEmpty(text))
            return;

        String photoUrl = null;
        if(user.getPhotoUrl()!=null)
            photoUrl = user.getPhotoUrl().toString();

        Comment comment = new Comment(user.getUid(), photoUrl, user.getDisplayName(), DateUtils.getFormattedCurrentDate(), text);

        if(commentsDatabaseReference!=null)
            commentsDatabaseReference.push().setValue(comment);

        binding.commentDBodyEdit.setText(null);

    }

    private void showHideCommentButton() {
        if(getUser()!=null) {
            binding.setUsername(getUser().getDisplayName());
            String photoUrl = null;
            if(getUser().getPhotoUrl()!=null)
                photoUrl = getUser().getPhotoUrl().toString();
            binding.setUserPhotoUrl(photoUrl);
        } else {
            binding.setUsername(null);
            binding.setUserPhotoUrl(null);
        }
    }


}
